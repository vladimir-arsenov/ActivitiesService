package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.client.YandexMapsClient;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.GetClosestActivityRequestsDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.PatchActivityRequestDto;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
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

    public List<ActivityRequestDto> getClosestActivityRequests(GetClosestActivityRequestsDto dto) {
        var activity = activityService.getActivityById(dto.getActivityId());
        var locationInfo = getLocationInfo(dto.getLocation());

        return activityRequestRepository.getClosest(activity.getId(), locationInfo.getCoordinates().toString(), dto.getRadius())
                .stream()
                .map(activityRequestMapper::toActivityRequestDto)
                .toList();
    }

    public ActivityRequestDto get(Long id) {
        return activityRequestMapper.toActivityRequestDto(getActivityRequestById(id));
    }

    public void add(CreateActivityRequestDto activityRequestDto) {
        var locationInfo = getLocationInfo(activityRequestDto.getLocation());
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
            var locationInfo = getLocationInfo(activityRequestDto.getLocation());
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

    private YandexMapsLocationResponse getLocationInfo(String location) {
        return location.matches("^[-+]?\\d+(\\.\\d+)?\\s+[-+]?\\d+(\\.\\d+)?$")
                ? yandexMapsClient.getLocationInfoByCoordinates(location)
                : yandexMapsClient.getLocationInfoByAddress(location);
    }

    private ActivityRequest getActivityRequestById(Long id) {
        return activityRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity request with id %d not found" .formatted(id)));
    }
}
