package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.example.tfintechgradproject.model.ActivityRequestStatus;
import org.example.tfintechgradproject.model.Role;
import org.example.tfintechgradproject.model.User;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ActivityRequestRepositoryTest {

    @Autowired
    private ActivityRequestRepository repository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityCategoryRepository activityCategoryRepository;

    private final WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 4326));

    private static final double ONE_DEGREE_OF_LATITUDE_IN_KM = 112000;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void getClosest_basedOnRadiusAndCoordinates_shouldReturnClosestActivityRequests() throws ParseException {
        var activity = activityRepository.save(
                new Activity(0L, "AR1", activityCategoryRepository.save(new ActivityCategory(1L, "name"))
        ));
        var user = userRepository.save(
                User.builder()
                        .rating(new BigDecimal("24"))
                        .email("e@mail")
                        .nickname("nickname")
                        .password("passsword").
                        role(Role.USER)
                        .build()
        );

        var activityRequests = repository.saveAll(List.of(
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 1.0)"))
                        .address("AR1")
                        .participantsRequired(1)
                        .status(ActivityRequestStatus.ACTIVE)
                        .creator(user)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 2.0)"))
                        .address("AR2")
                        .participantsRequired(1)
                        .status(ActivityRequestStatus.CLOSED)
                        .creator(user)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 3.0)"))
                        .address("AR3")
                        .participantsRequired(1)
                        .status(ActivityRequestStatus.ACTIVE)
                        .creator(user)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 4.0)"))
                        .address("AR4")
                        .participantsRequired(1)
                        .status(ActivityRequestStatus.ACTIVE)
                        .creator(user)
                        .build()));

        final var closestInOneDegreeRadius = repository.findActiveClosestActivityRequests(activity.getId(), "POINT(13.13 2.0)", ONE_DEGREE_OF_LATITUDE_IN_KM);
        final var closetInLessThanDegreeRadius = repository.findActiveClosestActivityRequests(activity.getId(), "POINT(13.13 3.0)", ONE_DEGREE_OF_LATITUDE_IN_KM-2000);
        assertAll(
                () -> assertEquals(2, closestInOneDegreeRadius.size()),
                () -> assertTrue(closestInOneDegreeRadius.contains(activityRequests.get(0))),
                () -> assertTrue(closestInOneDegreeRadius.contains(activityRequests.get(2))),
                () -> assertEquals(1, closetInLessThanDegreeRadius.size()),
                () -> assertTrue(closetInLessThanDegreeRadius.contains(activityRequests.get(2)))
        );
    }
}
