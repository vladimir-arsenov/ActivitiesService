package org.example.notificationservice.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class NotificationDto {
    private final String to;
    private final String subject;
    private final String text;
}
