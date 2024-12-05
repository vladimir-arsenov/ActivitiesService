package org.example.tfintechgradproject.mapper;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.ActivityRequestPreviewDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.example.tfintechgradproject.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ActivityRequestMapper {

    public ActivityRequest toActivityRequest(CreateActivityRequestDto activityRequestDto, Activity activity, YandexMapsLocationResponse yandexMapsLocationResponse, User creator){
        return ActivityRequest.builder()
                .activity(activity)
                .address(yandexMapsLocationResponse.getAddress())
                .coordinates(yandexMapsLocationResponse.getCoordinates())
                .comment(activityRequestDto.getComment())
                .participantsRequired(activityRequestDto.getParticipantsRequired())
                .participants(new ArrayList<>())
                .creator(creator)
                .build();
    }

    public ActivityRequestPreviewDto toActivityRequestPreviewDto(ActivityRequest activityRequest) {
        return ActivityRequestPreviewDto.builder()
                .activityId(activityRequest.getActivity().getId())
                .activityName(activityRequest.getActivity().getName())
                .categoryId(activityRequest.getActivity().getCategory().getId())
                .categoryName(activityRequest.getActivity().getCategory().getName())
                .address(activityRequest.getAddress())
                .coordinates("%s %s" .formatted(activityRequest.getCoordinates().getX(), activityRequest.getCoordinates().getY()))
                .comment(activityRequest.getComment())
                .participantsRequired(activityRequest.getParticipantsRequired())
                .participantsJoined(activityRequest.getParticipantsRequired() - activityRequest.getParticipants().size())
                .activityStart(activityRequest.getActivityStart())
                .joinDeadline(activityRequest.getJoinDeadline())
                .creatorId(activityRequest.getCreator().getId())
                .creatorNickname(activityRequest.getCreator().getNickname())
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
                .participantsJoined(activityRequest.getParticipantsRequired() - activityRequest.getParticipants().size())
                .activityStart(activityRequest.getActivityStart())
                .joinDeadline(activityRequest.getJoinDeadline())
                .creatorId(activityRequest.getCreator().getId())
                .creatorNickname(activityRequest.getCreator().getNickname())
                .participants(activityRequest.getParticipants().stream().map(User::getId).toList())
                .build();
    }
}
