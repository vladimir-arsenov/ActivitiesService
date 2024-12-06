package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.response.ActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.CreateActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.PatchActivityCategoryDto;
import org.example.tfintechgradproject.mapper.ActivityCategoryMapper;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.example.tfintechgradproject.repository.ActivityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityCategoryService {

    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityCategoryMapper activityCategoryMapper;


    public List<ActivityCategoryDto> getAll() {
        return activityCategoryRepository.findAll().stream()
                .map(activityCategoryMapper::toActivityCategoryDto)
                .toList();
    }

    public ActivityCategoryDto get(Long id) {
        return activityCategoryMapper.toActivityCategoryDto(getActivityCategoryById(id));
    }

    public void add(CreateActivityCategoryDto activityCategoryDto) {
        activityCategoryRepository.save(activityCategoryMapper.toActivityCategory(activityCategoryDto));
    }

    public void patch(Long id, PatchActivityCategoryDto activityCategoryDto) {
        var activityCategory = getActivityCategoryById(id);
        if (activityCategoryDto.getCategoryName() != null) {
            activityCategory.setName(activityCategoryDto.getCategoryName());
        }
        activityCategoryRepository.save(activityCategory);
    }

    public void delete(Long id) {
        var activity = getActivityCategoryById(id);
        activityCategoryRepository.delete(activity);
    }

    public ActivityCategory getActivityCategoryById(Long id) {
        return activityCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity category with id %d not found".formatted(id)));
    }
}
