package org.example.notifyservice.consumer;

import org.example.notifyservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCreatedConsumer {
    private final EmailService emailService;

    public AppointmentCreatedConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${notification.kafka.order-created-topic:appointment-created}")
    public void consume(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        String cleaned = email.trim();

        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        if (cleaned.isBlank()) {
            return;
        }
        emailService.sendOrderCreatedEmail(cleaned);
    }
}
