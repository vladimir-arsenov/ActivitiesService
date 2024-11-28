package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosestActivityRequestsDto {
    private Double radius;
    private String location;
    private Long activityId;
}

