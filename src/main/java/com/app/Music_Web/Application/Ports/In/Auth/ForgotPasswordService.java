package com.app.Music_Web.Application.Ports.In.Auth;

public interface ForgotPasswordService {
    void sendResetLink(String email);
    void resetPassword(String token, String newPassword);
}
