package org.example.tfintechgradproject.mapper;

import org.example.tfintechgradproject.dto.ActivityDto;
import org.example.tfintechgradproject.model.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityDto toActivityDto(Activity activity) {
        return ActivityDto.builder()
                .name(activity.getName())
                .category(activity.getCategory().getName())
                .build();
    }
}
