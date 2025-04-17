package com.app.Music_Web.Application.Ports.In.Mail;

public interface MailService {
    void sendResetPasswordEmail(String to, String resetLink);
    void sendVerificationEmail(String to, String verifyLink);
}
