package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityRequestDto {

    private ActivityDto activity;

    private String address;

    private String coordinates;

    private String comment;

    private Integer participantsRequired;
}
