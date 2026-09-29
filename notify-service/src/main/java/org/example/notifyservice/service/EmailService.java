package org.example.notifyservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${notification.mail.from:}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendOrderCreatedEmail(String recipient) {
        if (recipient == null || recipient.isBlank()) {
            return;
        }
        String to = recipient.trim();
        if (to.isBlank()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setTo(to);
        message.setSubject("Dat lich kham thanh cong");
        message.setText("Lich kham cua ban da duoc tao thanh cong. "
                + "Cam on ban da su dung dich vu dat lich kham benh.");
        mailSender.send(message);
    }
}
