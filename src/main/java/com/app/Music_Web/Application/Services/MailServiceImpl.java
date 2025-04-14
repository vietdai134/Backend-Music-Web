package com.app.Music_Web.Application.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.Ports.In.Mail.MailService;

@Service
public class MailServiceImpl implements MailService{
    private JavaMailSender mailSender;
    public MailServiceImpl(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }
    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        // message.setText("Click the following link to reset your password:\n" + resetLink);

        message.setText(
            "Xin chào,\n\n" +
            "Chúng tôi nhận được yêu cầu đặt lại mật khẩu từ bạn.\n" +
            "Vui lòng click vào liên kết sau để đặt lại mật khẩu (hạn sử dụng 15 phút):\n\n" +
            resetLink + "\n\n" +
            "Nếu bạn không yêu cầu, vui lòng bỏ qua email này.\n\n" +
            "Cảm ơn,\nMusic Web Team"
        );

        mailSender.send(message);
    }
}
