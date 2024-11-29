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

    private Long activityId;

    private String activityName;

    private Long categoryId;

    private String categoryName;

    private String address;

    private String coordinates;

    private String comment;

    private Integer participantsRequired;
}
