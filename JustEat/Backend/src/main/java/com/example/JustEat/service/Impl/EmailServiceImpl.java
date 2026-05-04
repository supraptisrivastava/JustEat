package com.example.JustEat.service.Impl;

import com.example.JustEat.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        String subject = "JustEat - Password Reset Request";
        String text = "Hello,\n\n"
                + "You requested to reset your password.\n\n"
                + "Click the link below to reset your password:\n"
                + resetLink + "\n\n"
                + "This link will expire in 15 minutes.\n\n"
                + "If you didn't request this, please ignore this email.\n\n"
                + "Thanks,\nJustEat Team";
        
        sendEmail(to, subject, text);
    }
}