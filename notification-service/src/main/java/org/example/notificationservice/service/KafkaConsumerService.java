package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.NotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MailNotificationService mailNotificationService;

    @KafkaListener(topics = "${application.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NotificationDto message) {
        log.debug("Received message for sending email notification to {} with subject {}", message.getTo(), message.getSubject());
        mailNotificationService.sendEmail(message.getTo(), message.getSubject(), message.getText());
    }
}
