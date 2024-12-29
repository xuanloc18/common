package dev.cxl.iam_service.application.service.custom;

import dev.cxl.iam_service.application.dto.identity.*;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.ResetPassword;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;

public interface UserKCLService {

    String createUserKCL(UserCreationRequest request);

    TokenExchangeResponse tokenExchangeResponse();

    TokenExchangeResponseUser tokenExchangeResponseUser(AuthenticationRequest authenticationRequest);

    void logOut(String token, String refreshToken);

    TokenExchangeResponseUser refreshToken(String refreshToken);

    void enableUser(String token, String id, UserUpdateRequest request);

    Boolean resetPassWord(String token, String id, ResetPassword request);
}
