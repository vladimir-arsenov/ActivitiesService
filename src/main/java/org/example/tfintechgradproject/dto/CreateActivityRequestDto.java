package org.example.tfintechgradproject.dto;

import lombok.Data;
import org.example.tfintechgradproject.model.Activity;

@Data
public class CreateActivityRequestDto {

    private Activity activity;

    private String location;

    private String comment;

    private Integer participantsRequired;
}