package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;



}
