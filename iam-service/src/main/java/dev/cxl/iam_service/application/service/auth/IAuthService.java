package dev.cxl.iam_service.application.service.auth;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;

import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.ResetPassword;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;

public interface IAuthService {
    Object login(AuthenticationRequest authenticationRequest);

    boolean logout(String token, String refreshToken) throws ParseException, JOSEException;

    boolean register(UserCreationRequest request);

    TokenExchangeResponseUser getRefreshToken(String refreshToken) throws ParseException, JOSEException;

    Boolean enableUser(String token, String id, UserUpdateRequest request) throws ParseException;

    Boolean resetPassword(String token, String id, ResetPassword resetPassword) throws ParseException;
}
