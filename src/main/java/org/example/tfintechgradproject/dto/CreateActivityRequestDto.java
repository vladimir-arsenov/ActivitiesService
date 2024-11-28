package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tfintechgradproject.model.Activity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequestDto {

    private Activity activity;

    private String location;

    private String comment;

    private Integer participantsRequired;
}