package org.example.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationKafkaPublisherService {

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Value("${application.kafka.topic}")
    private String topic;

    public void publish(NotificationDto notification) {
        log.debug("Sending message [{}] to kafka topic [{}]", notification, topic);

        kafkaTemplate.send(topic, notification);
    }
}
