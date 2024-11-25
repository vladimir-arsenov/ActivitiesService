package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.client.YandexMapsClient;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.ClosestActivityRequestsDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.example.tfintechgradproject.mapper.ActivityRequestMapper;
import org.example.tfintechgradproject.repository.ActivityRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityRequestService {

    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRequestMapper activityRequestMapper;
    private final YandexMapsClient yandexMapsClient;


    public void add(CreateActivityRequestDto activityRequestDto) {
        var locationInfo = getLocationInfo(activityRequestDto.getLocation());

        activityRequestRepository.save(activityRequestMapper.toActivityRequest(activityRequestDto, locationInfo));
    }



    public List<ActivityRequestDto> getClosestActivityRequests(ClosestActivityRequestsDto closestActivityRequestsDto) {
        var locationInfo = getLocationInfo(closestActivityRequestsDto.getLocation());
        return activityRequestRepository.getClosest(locationInfo.getCoordinates(), closestActivityRequestsDto.getRadius())
                .stream()
                .map(activityRequestMapper::toActivityRequestDto)
                .toList();
    }

    private YandexMapsLocationResponse getLocationInfo(String location) {
        return location.matches("^[-+]?\\d+(\\.\\d+)?\\s+[-+]?\\d+(\\.\\d+)?$")
                ? yandexMapsClient.getLocationInfoByCoordinates(location)
                : yandexMapsClient.getLocationInfoByAddress(location);
    }
}
