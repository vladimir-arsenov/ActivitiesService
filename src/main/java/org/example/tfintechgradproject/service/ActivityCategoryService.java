package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.ActivityCategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityCategoryService {
    private final ActivityCategoryRepository activityCategoryRepository;

}
