package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.response.ActivityDto;
import org.example.tfintechgradproject.dto.request.CreateActivityDto;
import org.example.tfintechgradproject.dto.request.PatchActivityDto;
import org.example.tfintechgradproject.mapper.ActivityMapper;
import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ActivityCategoryService activityCategoryService;

    public List<ActivityDto> getAll() {
        return activityRepository.findAll().stream()
                .map(activityMapper::toActivityDto)
                .toList();
    }

    public List<ActivityDto> getActivitiesByCategory(Long categoryId) {
        return activityRepository.findActivityByCategoryId(categoryId).stream()
                .map(activityMapper::toActivityDto)
                .toList();
    }

    public ActivityDto get(Long id) {
        return activityMapper.toActivityDto(getActivityById(id));
    }

    public void add(CreateActivityDto activityDto) {
        activityRepository.save(activityMapper.toActivity(activityDto, activityCategoryService.getActivityCategoryById(activityDto.getCategoryId())));
    }

    public void patch(Long id, PatchActivityDto activityDto) {
        var activity = getActivityById(id);
        if (activityDto.getActivityName() != null) {
            activity.setName(activityDto.getActivityName());
        }
        if (activityDto.getCategoryId() != null) {
            activity.setCategory(activityCategoryService.getActivityCategoryById(activityDto.getCategoryId()));
        }
        activityRepository.save(activity);
    }

    public void delete(Long id) {
        var activity = getActivityById(id);
        activityRepository.delete(activity);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id %s not found".formatted(id)));
    }
}
