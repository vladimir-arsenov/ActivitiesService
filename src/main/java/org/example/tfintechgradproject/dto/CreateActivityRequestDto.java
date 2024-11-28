package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequestDto {

    private Long activityId;

    private String location;

    private String comment;

    private Integer participantsRequired;
}