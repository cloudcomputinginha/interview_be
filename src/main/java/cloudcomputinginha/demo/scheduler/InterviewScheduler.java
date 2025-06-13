package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.scheduler.job.InterviewSchedulerJob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewScheduler {

    @Getter
    private final Scheduler scheduler;

    public void scheduleInterviewStart(Long interviewId, LocalDateTime scheduledTime) {

        if (interviewId == null || scheduledTime == null) {
            throw new IllegalArgumentException("Interview id and scheduled time cannot be null");
        }

        if(scheduledTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Interview id and scheduled time cannot be before current time");
        }

        try {
            JobDetail jobDetail = JobBuilder.newJob(InterviewSchedulerJob.class)
                .withIdentity("interview-start-job-" + interviewId)
                .usingJobData("interviewId", interviewId)
                .storeDurably()
                .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("interview-start-trigger-" + interviewId)
                .startAt(Timestamp.valueOf(scheduledTime))
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule interview start job", e);
        }
    }

    public void cancelScheduledInterview(Long interviewId) {

        if(interviewId == null) {
            throw new IllegalArgumentException("Interview id cannot be null");
        }

        try {
            log.info("Canceling scheduled interview for interviewId: {}", interviewId);
            JobKey jobKey = new JobKey("interview-start-job-" + interviewId);
            TriggerKey triggerKey = new TriggerKey("interview-start-trigger-" + interviewId);

            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            log.info("Successfully canceled scheduled interview for interviewId: {}", interviewId);
        } catch (SchedulerException e) {
            log.error("Failed to cancel scheduled interview for interviewId: {}", interviewId, e);
            throw new RuntimeException("Failed to cancel scheduled interview for interviewId: " + interviewId, e);
        }
    }
}