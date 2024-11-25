package org.example.tfintechgradproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDto {

    private String name;

    private String category;
}
