package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.ActivityRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityRepository activityRepository;
}
