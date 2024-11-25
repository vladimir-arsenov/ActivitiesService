package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.ClosestActivityRequestsDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.service.ActivityRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityRequestController {

    private final ActivityRequestService activityRequestService;

    @GetMapping
    public List<ActivityRequestDto> getClosestActivityRequests(@RequestBody ClosestActivityRequestsDto closestActivityRequestsDto) {
        return activityRequestService.getClosestActivityRequests(closestActivityRequestsDto);
    }

    @PostMapping
    public void add(@RequestBody CreateActivityRequestDto activityRequestDto) {
        activityRequestService.add(activityRequestDto);
    }
}
