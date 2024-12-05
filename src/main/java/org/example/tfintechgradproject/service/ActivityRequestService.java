package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.client.YandexMapsClient;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.ActivityRequestPreviewDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.PatchActivityRequestDto;
import org.example.tfintechgradproject.exception.exceptions.CannotJoinActivityRequest;
import org.example.tfintechgradproject.mapper.ActivityRequestMapper;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.example.tfintechgradproject.model.ActivityRequestStatus;
import org.example.tfintechgradproject.repository.ActivityRequestRepository;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityRequestService {

    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRequestMapper activityRequestMapper;
    private final YandexMapsClient yandexMapsClient;
    private final ActivityService activityService;
    private final UserService userService;

    public List<ActivityRequestPreviewDto> getClosestActivityRequests(Long activityId, String location, Double radius) {
        var activity = activityService.getActivityById(activityId);
        var locationInfo =  yandexMapsClient.getLocationInfo(location);

        return activityRequestRepository.findClosestAndActive(activity.getId(), locationInfo.getCoordinates().toString(), radius)
                .stream()
                .map(activityRequestMapper::toActivityRequestPreviewDto)
                .toList();
    }

    public void join(Long id, UserPrincipal authenticatedUser) {
        var user = userService.findByEmail(authenticatedUser.getUsername());
        var activityRequest = getActivityRequestById(id);
        var participants = activityRequest.getParticipants();

        if (!activityRequest.getStatus().equals(ActivityRequestStatus.ACTIVE) || activityRequest.getParticipantsRequired() - participants.size() < 1) {
            throw new CannotJoinActivityRequest("Activity request is not active anymore or all spots are filled");
        }

        participants.add(user);
        activityRequest.setParticipants(participants);
        activityRequestRepository.save(activityRequest);
    }

    public ActivityRequestDto get(Long id) {
        return activityRequestMapper.toActivityRequestDto(getActivityRequestById(id));
    }

    public ActivityRequestPreviewDto getPreview(Long id) {
        return activityRequestMapper.toActivityRequestPreviewDto(getActivityRequestById(id));
    }

    public void add(CreateActivityRequestDto activityRequestDto, UserPrincipal authenticatedUser) {
        var locationInfo = yandexMapsClient.getLocationInfo(activityRequestDto.getLocation());
        var activity = activityService.getActivityById(activityRequestDto.getActivityId());
        var creator = userService.findByEmail(authenticatedUser.getUsername());
        activityRequestRepository.save(activityRequestMapper.toActivityRequest(activityRequestDto, activity, locationInfo, creator));
    }

    public void patch(Long id, PatchActivityRequestDto activityRequestDto) {
        var activityRequest = getActivityRequestById(id);
        if (activityRequestDto.getStatus() != null) {
            activityRequest.setStatus(ActivityRequestStatus.valueOf(activityRequestDto.getStatus()));
        }
        if (activityRequestDto.getActivityId() != null) {
            activityRequest.setActivity(activityService.getActivityById(activityRequestDto.getActivityId()));
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
        if (activityRequestDto.getJoinDeadline() != null) {
            activityRequest.setJoinDeadline(activityRequestDto.getJoinDeadline());
        }
        if (activityRequestDto.getActivityStart() != null) {
            activityRequest.setActivityStart(activityRequestDto.getActivityStart());
        }
        activityRequestRepository.save(activityRequest);
    }

    public void delete(Long id) {
        var activityRequest = getActivityRequestById(id);
        activityRequestRepository.delete(activityRequest);
    }

    public boolean isOwner(Authentication authentication, Long id) {
        return getActivityRequestById(id).getCreator().getEmail().equals(authentication.getName());
    }

    private ActivityRequest getActivityRequestById(Long id) {
        return activityRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity request with id %d not found" .formatted(id)));
    }
}
