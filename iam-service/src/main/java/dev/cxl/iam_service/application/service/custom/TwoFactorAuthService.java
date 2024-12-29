package dev.cxl.iam_service.application.service.custom;

import com.evo.common.exception.AppException;

import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequestTwo;

public interface TwoFactorAuthService {

    String generateOtp();

    void sendOtpMail(AuthenticationRequest authenticationRequest) throws AppException;

    void sendCreatUser(String email);

    Boolean validateOtp(AuthenticationRequestTwo authenticationRequestTwo);
}
