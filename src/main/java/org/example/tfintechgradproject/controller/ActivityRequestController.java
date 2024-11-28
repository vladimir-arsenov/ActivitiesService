package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.ClosestActivityRequestsDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.PatchActivityRequestDto;
import org.example.tfintechgradproject.service.ActivityRequestService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/activity-requests")
@RequiredArgsConstructor
public class ActivityRequestController {

    private final ActivityRequestService activityRequestService;

    @GetMapping
    public List<ActivityRequestDto> getClosestActivityRequests(@RequestBody ClosestActivityRequestsDto closestActivityRequestsDto) {
        return activityRequestService.getClosestActivityRequests(closestActivityRequestsDto);
    }

    @GetMapping("/{id}")
    public ActivityRequestDto get(@PathVariable Long id) {
        return activityRequestService.get(id);
    }

    @PostMapping
    public void add(@RequestBody CreateActivityRequestDto activityRequestDto) {
        activityRequestService.add(activityRequestDto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id, @RequestBody PatchActivityRequestDto activityRequestDto) {
        activityRequestService.patch(id, activityRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityRequestService.delete(id);
    }
}
