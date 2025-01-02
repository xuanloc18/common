package dev.cxl.iam_service.application.service.impl;

import java.util.List;

import dev.cxl.iam_service.domain.command.UserCreationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.cxl.iam_service.application.dto.identity.*;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.ResetPassword;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.service.auth.IndentityClient;
import dev.cxl.iam_service.application.service.custom.UserKCLService;
import lombok.experimental.NonFinal;

@Service
public class UserKCLServiceImpl implements UserKCLService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final IndentityClient indentityClient;

    @Value("${idp.client-id}")
    @NonFinal
    String idpClientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String idpClientSecret;

    public UserKCLServiceImpl(IndentityClient indentityClient, UtilUserServiceImpl utilUser) {
        this.indentityClient = indentityClient;
    }

    public String createUserKCL(UserCreationRequest request) {

        var creationResponse = indentityClient.createUser(
                "Bearer " + tokenExchangeResponse().getAccessToken(),
                UserCreationParam.builder()
                        .username(request.getUserName())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getUserMail())
                        .enabled(true)
                        .emailVerified(true)
                        .credentials(List.of(Credential.builder()
                                .type("password")
                                .temporary(false)
                                .value(request.getPassWord())
                                .build()))
                        .build());
        String userKCLID = extractUserId(creationResponse);
        log.info("userKCLID {}", userKCLID);
        return userKCLID;
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] parts = location.split("/");
        return parts[parts.length - 1];
    }

    public TokenExchangeResponse tokenExchangeResponse() {
        return indentityClient.exchangToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(idpClientId)
                .client_secret(idpClientSecret)
                .scope("openid")
                .build());
    }

    public TokenExchangeResponseUser tokenExchangeResponseUser(AuthenticationRequest authenticationRequest) {
        return indentityClient.exchangTokenUser(TokenExchangeParamUser.builder()
                .grant_type("password")
                .client_id(idpClientId)
                .client_secret(idpClientSecret)
                .username(authenticationRequest.getUserMail())
                .password(authenticationRequest.getPassWord())
                .scope("openid")
                .build());
    }

    public void logOut(String token, String refreshToken) {
        token = "Bearer" + token;
        Logout logout = Logout.builder()
                .refresh_token(refreshToken)
                .client_id(idpClientId)
                .client_secret(idpClientSecret)
                .build();
        indentityClient.logoutUser(token, logout);
    }

    public TokenExchangeResponseUser refreshToken(String refreshToken) {
        TokenExchangeRefresh build = TokenExchangeRefresh.builder()
                .grant_type("refresh_token")
                .refresh_token(refreshToken)
                .client_id(idpClientId)
                .client_secret(idpClientSecret)
                .build();
        return indentityClient.refrehToken(build);
    }

    public void enableUser(String token, String id, UserUpdateRequest request) {
        token = "Bearer " + token;
        indentityClient.enableUser(token, id, request);
    }

    public Boolean resetPassWord(String token, String id, ResetPassword request) {
        token = "Bearer " + tokenExchangeResponse().getAccessToken().toString();
        indentityClient.resetPassWord(token, id, request);
        return true;
    }
}
