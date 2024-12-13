package org.example.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.activityservice.utils.CloseActivityRequestJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobService {

    private final Scheduler scheduler;

    public void scheduleCloseActivityRequestJob(Long requestId, LocalDateTime deadline) {
        log.debug("Creating quartz job to close activity request {} at {}", requestId, deadline);

        try {
            JobDetail jobDetail = JobBuilder.newJob(CloseActivityRequestJob.class)
                    .withIdentity("closeRequestJob_%d".formatted(requestId), "activityRequests")
                    .usingJobData("requestId", requestId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("closeRequestJob_%d".formatted(requestId), "activityRequests")
                    .startAt(Date.from(deadline.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            log.debug("Scheduled quartz job to close activity request {} at {}", requestId, deadline);
        } catch (SchedulerException e) {
            log.error("Failed to create quartz job to close activity request {} at {}", requestId, deadline);
        }

    }
}
