package org.example.activityservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class NotificationDto {
    private final String to;
    private final String subject;
    private final String text;
}
