package org.example.tfintechgradproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityRequestDto {

    private ActivityDto activity;

    private String address;

    private String coordinates;

    private String comment;

    private Integer participantsRequired;
}
