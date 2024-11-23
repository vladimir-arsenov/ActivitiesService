package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.service.ActivityCategoryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActivityCategoryController {
    private final ActivityCategoryService activityCategoryService;
}
