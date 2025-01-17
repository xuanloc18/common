package dev.cxl.iam_service.presentation.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.BasedResponse;
import com.evo.common.webapp.security.AuthorityService;
import com.nimbusds.jose.JOSEException;

import dev.cxl.iam_service.application.configuration.IdpConfig;
import dev.cxl.iam_service.application.configuration.KeyProvider;
import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequestTwo;
import dev.cxl.iam_service.application.dto.request.ForgotPassWord;
import dev.cxl.iam_service.application.dto.request.IntrospectRequest;
import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.dto.response.AuthenticationResponse;
import dev.cxl.iam_service.application.dto.response.DefaultClientTokenResponse;
import dev.cxl.iam_service.application.dto.response.IntrospectResponse;
import dev.cxl.iam_service.application.service.impl.AuthenticationServiceImpl;
import dev.cxl.iam_service.application.service.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    private final UserServiceImpl userService;

    private final IdpConfig idpConfig;

    private final KeyProvider keyProvider;

    private final AuthorityService authorityService;

    @PostMapping("/login")
    public APIResponse<Object> login(@RequestBody AuthenticationRequest authenticationRequest) throws IOException {
        return APIResponse.<Object>builder()
                .result(idpConfig.getAuthService().login(authenticationRequest))
                .build();
    }

    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> authenticationResponseAPIResponse(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return APIResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    APIResponse<Void> logout(
            @RequestHeader("authorization") String token, @RequestParam("refreshToken") String refreshToken)
            throws ParseException, JOSEException {
        idpConfig.getAuthService().logout(token, refreshToken);
        return APIResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    APIResponse<TokenExchangeResponseUser> refresh(@RequestParam("refreshToken") String refreshToken) throws Exception {
        return APIResponse.<TokenExchangeResponseUser>builder()
                .result(idpConfig.getAuthService().getRefreshToken(refreshToken))
                .build();
    }

    @PostMapping("/send-email")
    APIResponse<String> sendemail(@RequestParam("email") String email) {
        userService.sendToken(email);
        return APIResponse.<String>builder().result("Chuc ban thanh cong").build();
    }

    @PutMapping("/forgot-pass")
    APIResponse<Boolean> forgotPass(@RequestBody ForgotPassWord forgotPassWord) throws ParseException, JOSEException {

        return APIResponse.<Boolean>builder()
                .result(userService.check(forgotPassWord))
                .build();
    }

    @PostMapping("/tfa-two")
    APIResponse<AuthenticationResponse> authenticationResponseAPIResponse(@RequestBody AuthenticationRequestTwo two)
            throws ParseException {
        var result = authenticationService.authenticate(two);
        return APIResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @GetMapping("/client-token/{clientId}/{clientSecret}")
    public String getClientToken(DefaultClientTokenResponse request) {
        return authenticationService.generateClientToken(request);
    }

    @GetMapping("/api/certificate/.well-known/jwks.json")
    Map<String, Object> keys() {
        return this.keyProvider.jwkSet().toJSONObject();
    }

    @GetMapping("/{userid}/authorities-by-userid")
    BasedResponse<UserAuthority> getUserAuthority(@PathVariable UUID userid) {
        return BasedResponse.success(
                "Get authorities successful for user" + userid, authorityService.getUserAuthority(userid));
    }

    @GetMapping("/{clientId}/authorities-by-clientId")
    BasedResponse<UserAuthority> getClientAuthority(@PathVariable String clientId) {
        return BasedResponse.success(
                "Get authorities successful for client " + clientId, authorityService.getClientAuthority(clientId));
    }
}
