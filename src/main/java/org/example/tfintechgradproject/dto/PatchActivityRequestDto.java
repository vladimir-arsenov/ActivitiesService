package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchActivityRequestDto {

    private Long activityId;

    private String location;

    private String comment;

    private Integer participantsRequired;

    private String status;

    private LocalDateTime joinDeadline;

    private LocalDateTime activityStart;
}