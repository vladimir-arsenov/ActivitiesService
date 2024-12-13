package org.example.activityservice.mapper;

import org.example.activityservice.dto.response.ActivityCategoryDto;
import org.example.activityservice.dto.request.CreateActivityCategoryDto;
import org.example.activityservice.model.ActivityCategory;
import org.springframework.stereotype.Component;

@Component
public class ActivityCategoryMapper {

    public ActivityCategoryDto toActivityCategoryDto(ActivityCategory activityCategory) {
        return ActivityCategoryDto.builder()
                .categoryId(activityCategory.getId())
                .categoryName(activityCategory.getName())
                .build();
    }

    public ActivityCategory toActivityCategory(CreateActivityCategoryDto activityCategoryDto) {
        return ActivityCategory.builder()
                .name(activityCategoryDto.getCategoryName())
                .build();
    }
}
