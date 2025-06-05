package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.scheduler.job.InterviewSchedulerJob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewScheduler {

    @Getter
    private final Scheduler scheduler;

    public void scheduleInterviewStart(Long interviewId, LocalDateTime scheduledTime) {
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
        try {
            JobKey jobKey = new JobKey("interview-start-job-" + interviewId);
            TriggerKey triggerKey = new TriggerKey("interview-start-trigger-" + interviewId);

            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to cancel scheduled interview", e);
        }
    }
}