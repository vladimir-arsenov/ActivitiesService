package org.example.activityservice.service;

import org.example.activityservice.client.YandexMapsClient;
import org.example.activityservice.dto.request.CreateActivityRequestDto;
import org.example.activityservice.dto.response.ActivityRequestPreviewDto;
import org.example.activityservice.dto.response.YandexMapsLocationResponse;
import org.example.activityservice.exception.exceptions.CannotJoinActivityRequestException;
import org.example.activityservice.mapper.ActivityRequestMapper;
import org.example.activityservice.model.Activity;
import org.example.activityservice.model.ActivityCategory;
import org.example.activityservice.model.ActivityRequest;
import org.example.activityservice.model.ActivityRequestStatus;
import org.example.activityservice.model.User;
import org.example.activityservice.repository.ActivityRequestRepository;
import org.example.activityservice.security.auth.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActivityRequestServiceTest {

    @Mock
    private YandexMapsClient yandexMapsClientMock;

    @Mock
    private ActivityService activityServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private ActivityRequestRepository activityRequestRepository;

    @Mock
    private ActivityRequestMapper activityRequestMapperMock;

    @Mock
    private UserPrincipal userPrincipalMock;

    @InjectMocks
    private ActivityRequestService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void join_activeActivityRequest_shouldAddUserToParticipants() {
        var activityRequest = ActivityRequest.builder()
                .id(1L)
                .status(ActivityRequestStatus.ACTIVE)
                .participantsRequired(5)
                .participants(new ArrayList<>(Arrays.asList(new User(), new User())))
                .build();
        when(activityRequestRepository.findById(any())).thenReturn(Optional.of(activityRequest));

        service.join(activityRequest.getId(), userPrincipalMock);

        var captor = ArgumentCaptor.forClass(ActivityRequest.class);
        verify(activityRequestRepository, times(1)).save(captor.capture());
        assertEquals(activityRequest, captor.getValue());
    }

    @Test
    public void join_closedActivityRequest_shouldThrowCannotJoinActivityRequestException() {
        var activityRequest = ActivityRequest.builder()
                .id(1L)
                .status(ActivityRequestStatus.CLOSED)
                .participantsRequired(3)
                .participants(new ArrayList<>(Arrays.asList(null, null)))
                .build();
        when(activityRequestRepository.findById(any())).thenReturn(Optional.of(activityRequest));

        var exception = assertThrows(CannotJoinActivityRequestException.class,
                () -> service.join(activityRequest.getId(), userPrincipalMock));

        assertEquals("Activity request is not active anymore, user is participant already or all spots are filled", exception.getMessage());
    }

    @Test
    public void join_allParticipantsAlreadySet_shouldThrowCannotJoinActivityRequestException() {
        var activityRequest = ActivityRequest.builder()
                .id(1L)
                .status(ActivityRequestStatus.ACTIVE)
                .participantsRequired(3)
                .participants(new ArrayList<>(Arrays.asList(null, null, null)))
                .build();
        when(activityRequestRepository.findById(any())).thenReturn(Optional.of(activityRequest));

        var exception = assertThrows(CannotJoinActivityRequestException.class,
                () -> service.join(activityRequest.getId(), userPrincipalMock));

        assertEquals("Activity request is not active anymore, user is participant already or all spots are filled", exception.getMessage());
    }


    @Test
    public void add_shouldSaveActivityRequest() {
        var activityRequest = new ActivityRequest();
        when(activityRequestMapperMock.toActivityRequest(any(), any(), any(), any())).thenReturn(activityRequest);

        service.add(new CreateActivityRequestDto(), userPrincipalMock);

        var captor = ArgumentCaptor.forClass(ActivityRequest.class);
        verify(activityRequestRepository, times(1)).save(captor.capture());
        assertEquals(activityRequest, captor.getValue());
    }

    @Test
    public void delete_shouldDeleteActivityRequest() {
        var activityRequest = new ActivityRequest();
        activityRequest.setId(1L);
        when(activityRequestRepository.findById(activityRequest.getId())).thenReturn(Optional.of(activityRequest));

        service.delete(activityRequest.getId());

        var captor = ArgumentCaptor.forClass(ActivityRequest.class);
        verify(activityRequestRepository, times(1)).delete(captor.capture());
        assertEquals(activityRequest, captor.getValue());
    }

    @Test
    public void isOwner_userIsOwner_shouldReturnTrue() {
        var requestCreator = User.builder()
                .email("email")
                .build();
        var activityRequest = ActivityRequest.builder()
                .id(1L)
                .creator(requestCreator)
                .build();
        var authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn(requestCreator.getEmail());
        when(activityRequestRepository.findById(activityRequest.getId())).thenReturn(Optional.of(activityRequest));

        var result = service.isOwner(authenticationMock, activityRequest.getId());

        assertTrue(result);
    }

    @Test
    public void isOwner_userIsNotOwner_shouldReturnFalse() {
        var requestCreator = User.builder()
                .email("email")
                .build();
        var activityRequest = ActivityRequest.builder()
                .id(1L)
                .creator(requestCreator)
                .build();
        var authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("not " + requestCreator.getEmail());
        when(activityRequestRepository.findById(activityRequest.getId())).thenReturn(Optional.of(activityRequest));

        var result = service.isOwner(authenticationMock, activityRequest.getId());

        assertFalse(result);
    }

    @Test
    public void getClosestActivityRequests_shouldReturnListOfActivityRequests() {
        var expectedResult = List.of(new ActivityRequestPreviewDto(), new ActivityRequestPreviewDto());
        when(activityRequestRepository.findActiveClosestActivityRequests(any(), any(), any()))
                .thenReturn(new ArrayList<>(Arrays.asList(null, null)));
        when(activityRequestMapperMock.toActivityRequestPreviewDto(any())).thenReturn(new ActivityRequestPreviewDto());
        when(activityServiceMock.getActivityById(any())).thenReturn(new Activity(1L, "", new ActivityCategory()));
        when(yandexMapsClientMock.getLocationInfo(any())).thenReturn(new YandexMapsLocationResponse("", new Point(null, new GeometryFactory())));

        var result = service.getClosestActivityRequests(0L, null, 0d);

        assertAll(
                () -> assertEquals(expectedResult.get(0), result.get(0)),
                () -> assertEquals(expectedResult.get(1), result.get(0))
        );
    }
}

