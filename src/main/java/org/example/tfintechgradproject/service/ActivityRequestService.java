package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.client.YandexMapsClient;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.PatchActivityRequestDto;
import org.example.tfintechgradproject.mapper.ActivityRequestMapper;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.example.tfintechgradproject.repository.ActivityRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityRequestService {

    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRequestMapper activityRequestMapper;
    private final YandexMapsClient yandexMapsClient;
    private final ActivityService activityService;

    public List<ActivityRequestDto> getClosestActivityRequests(Long activityId, String location, Double radius) {
        var activity = activityService.getActivityById(activityId);
        var locationInfo =  yandexMapsClient.getLocationInfo(location);

        return activityRequestRepository.getClosest(activity.getId(), locationInfo.getCoordinates().toString(), radius)
                .stream()
                .map(activityRequestMapper::toActivityRequestDto)
                .toList();
    }

    public ActivityRequestDto get(Long id) {
        return activityRequestMapper.toActivityRequestDto(getActivityRequestById(id));
    }

    public void add(CreateActivityRequestDto activityRequestDto) {
        var locationInfo = yandexMapsClient.getLocationInfo(activityRequestDto.getLocation());
        var activity = activityService.getActivityById(activityRequestDto.getActivityId());
        activityRequestRepository.save(activityRequestMapper.toActivityRequest(activityRequestDto, activity, locationInfo));
    }

    public void patch(Long id, PatchActivityRequestDto activityRequestDto) {
        var activityRequest = getActivityRequestById(id);
        if (activityRequestDto.getActivityId() != null) {
            activityRequest.setId(activityRequestDto.getActivityId());
        }
        if (activityRequestDto.getComment() != null) {
            activityRequest.setComment(activityRequestDto.getComment());
        }
        if (activityRequestDto.getLocation() != null) {
            var locationInfo = yandexMapsClient.getLocationInfo(activityRequestDto.getLocation());
            activityRequest.setAddress(locationInfo.getAddress());
            activityRequest.setCoordinates(locationInfo.getCoordinates());
        }
        if (activityRequestDto.getParticipantsRequired() != null) {
            activityRequest.setParticipantsRequired(activityRequestDto.getParticipantsRequired());
        }
        activityRequestRepository.save(activityRequest);
    }

    public void delete(Long id) {
        var activityRequest = getActivityRequestById(id);
        activityRequestRepository.delete(activityRequest);
    }

    private ActivityRequest getActivityRequestById(Long id) {
        return activityRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity request with id %d not found" .formatted(id)));
    }
}
