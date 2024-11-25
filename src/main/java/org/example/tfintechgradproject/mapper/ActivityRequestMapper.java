package org.example.tfintechgradproject.mapper;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ActivityRequestDto;
import org.example.tfintechgradproject.dto.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ActivityRequestMapper {
    private final WKTReader wktReader = new WKTReader();
    private final ActivityMapper activityMapper;


    public ActivityRequest toActivityRequest(CreateActivityRequestDto activityRequestDto, YandexMapsLocationResponse yandexMapsLocationResponse){
        Point point;
        try {
            point = (Point) wktReader.read("POINT (" + yandexMapsLocationResponse + ")");
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
        return ActivityRequest.builder()
                .activity(activityRequestDto.getActivity())
                .address(yandexMapsLocationResponse.getAddress())
                .coordinates(point)
                .comment(activityRequestDto.getComment())
                .participantsRequired(activityRequestDto.getParticipantsRequired())
                .participants(new ArrayList<>())
                .build();
    }

    public ActivityRequestDto toActivityRequestDto(ActivityRequest activityRequest) {
        return ActivityRequestDto.builder()
                .activity(activityMapper.toActivityDto(activityRequest.getActivity()))
                .address(activityRequest.getAddress())
                .coordinates(activityRequest.getCoordinates().getX() + " " + activityRequest.getCoordinates().getY())
                .comment(activityRequest.getComment())
                .participantsRequired(activityRequest.getParticipantsRequired())
                .build();
    }
}
