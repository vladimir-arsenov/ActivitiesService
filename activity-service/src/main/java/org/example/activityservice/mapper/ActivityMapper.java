package org.example.activityservice.mapper;

import org.example.activityservice.dto.response.ActivityDto;
import org.example.activityservice.dto.request.CreateActivityDto;
import org.example.activityservice.model.Activity;
import org.example.activityservice.model.ActivityCategory;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public ActivityDto toActivityDto(Activity activity) {
        return ActivityDto.builder()
                .activityId(activity.getId())
                .activityName(activity.getName())
                .categoryName(activity.getCategory().getName())
                .categoryId(activity.getCategory().getId())
                .build();
    }

    public Activity toActivity(CreateActivityDto activityDto, ActivityCategory category) {
        return Activity.builder()
                .name(activityDto.getActivityName())
                .category(category)
                .build();
    }
}
