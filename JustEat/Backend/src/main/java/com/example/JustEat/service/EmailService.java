package com.example.JustEat.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendPasswordResetEmail(String to, String token);
}