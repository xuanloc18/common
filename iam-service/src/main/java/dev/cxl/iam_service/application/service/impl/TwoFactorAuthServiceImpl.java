package dev.cxl.iam_service.application.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequestTwo;
import dev.cxl.iam_service.application.service.custom.TwoFactorAuthService;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    private final EmailServiceImpl emailService;

    private final UtilUserServiceImpl utilUser;

    private final RedisTemplate<String, String> redisTemplate;

    public TwoFactorAuthServiceImpl(
            EmailServiceImpl emailService, UtilUserServiceImpl utilUser, RedisTemplate<String, String> redisTemplate) {
        this.emailService = emailService;
        this.utilUser = utilUser;
        this.redisTemplate = redisTemplate;
    }

    public String generateOtp() {
        Integer otp = (100000 + (int) (Math.random() * 900000));
        return otp.toString();
    }

    public void sendOtpMail(AuthenticationRequest authenticationRequest) {
        UserEntity user = utilUser.finUserMail(authenticationRequest.getUserMail());
        if (!user.getEnabled()) {
            throw new AppException(ErrorCode.USER_DIS_ENABLE);
        }
        if (user.getDeleted()) {
            throw new AppException(ErrorCode.USER_DELETED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authentication = passwordEncoder.matches(authenticationRequest.getPassWord(), user.getPassWord());
        if (!authentication) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String otp = generateOtp();
        emailService.SendEmail(user.getUserMail(), otp);
        redisTemplate.opsForValue().set(user.getUserMail(), otp, 5, TimeUnit.MINUTES);
    }

    public void sendCreatUser(String email) {
        String otp = generateOtp();
        StringBuilder message = new StringBuilder();
        message.append("Click vào link thứ nhất để xác nhận đăng kí tài khoàn:<br>");
        message.append("<a href=\"http://localhost:8088/api/iam/users/confirm?email=");
        message.append(email);
        message.append("&otp=");
        message.append(otp);
        message.append("\">Xác nhận tài khoản</a><br>");
        emailService.SendEmail(email, message.toString());
        redisTemplate.opsForValue().set(email, otp, 5, TimeUnit.MINUTES);
    }

    public Boolean validateOtp(AuthenticationRequestTwo authenticationRequestTwo) {

        UserEntity user = utilUser.finUserMail(authenticationRequestTwo.getUserMail());
        String otp = redisTemplate.opsForValue().get(user.getUserMail());
        if (otp != null && otp.equals(authenticationRequestTwo.getOtp())) {
            redisTemplate.delete(authenticationRequestTwo.getUserMail()); // Xóa OTP khỏi Redis
            return true;
        } else {
            return false;
        }
    }
}
