package org.example.tfintechgradproject.dto;

import lombok.Data;

@Data
public class ClosestActivityRequestsDto {
    private Double radius;
    private String location;
    private ActivityDto activity;
}

