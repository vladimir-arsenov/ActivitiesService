package org.example.activityservice.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.NotificationDto;
import org.example.activityservice.model.ActivityRequest;
import org.example.activityservice.service.ActivityRequestService;
import org.example.activityservice.service.NotificationKafkaPublisherService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloseActivityRequestJob implements Job {

    private final ActivityRequestService activityRequestService;
    private final NotificationKafkaPublisherService notificationPublisherService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        var requestId = jobExecutionContext.getJobDetail().getJobDataMap().getLong("requestId");

        log.debug("Closing quartz job to close activity request {}", requestId);

        activityRequestService.closeActivityRequest(requestId);

        sendNotificationsToParticipants(activityRequestService.getActivityRequestById(requestId));

        log.debug("Closed quartz job to close activity request {}", requestId);
    }

    private void sendNotificationsToParticipants(ActivityRequest request) {
        for (var participant : request.getParticipants()) {
            var notification = NotificationDto.builder()
                    .to(participant.getEmail())
                    .subject("Activity request of %s".formatted(request.getActivity().getName()))
                    .text("Activity request of %s at %s is closed".formatted(request.getActivity().getName(), request.getActivityStart()))
                    .build();
            notificationPublisherService.publish(notification);
        }
    }
}
