package org.example.tfintechgradproject.mapper;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ActivityRequestMapper {

    public ActivityRequest toActivityRequest(CreateActivityRequestDto activityRequestDto, Activity activity, YandexMapsLocationResponse yandexMapsLocationResponse){
        return ActivityRequest.builder()
                .activity(activity)
                .address(yandexMapsLocationResponse.getAddress())
                .coordinates(yandexMapsLocationResponse.getCoordinates())
                .comment(activityRequestDto.getComment())
                .participantsRequired(activityRequestDto.getParticipantsRequired())
                .participants(new ArrayList<>())
                .build();
    }

    public ActivityRequestDto toActivityRequestDto(ActivityRequest activityRequest) {
        return ActivityRequestDto.builder()
                .activityId(activityRequest.getActivity().getId())
                .activityName(activityRequest.getActivity().getName())
                .categoryId(activityRequest.getActivity().getCategory().getId())
                .categoryName(activityRequest.getActivity().getCategory().getName())
                .address(activityRequest.getAddress())
                .coordinates("%s %s" .formatted(activityRequest.getCoordinates().getX(), activityRequest.getCoordinates().getY()))
                .comment(activityRequest.getComment())
                .participantsRequired(activityRequest.getParticipantsRequired())
                .build();
    }
}
