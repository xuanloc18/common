package dev.cxl.iam_service.application.service.custom;

public interface EmailService {

    void sendEmail(String to, String subject, String messageContent);
}
