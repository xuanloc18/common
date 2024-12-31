package dev.cxl.iam_service.application.service.custom;

public interface EmailService {

    void SendEmail(String email, String otp);
}
