package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.scheduler.job.InterviewReminderJob;
import cloudcomputinginha.demo.scheduler.job.InterviewSchedulerJob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewScheduler {

    @Getter
    private final Scheduler scheduler;

    public void scheduleInterviewStart(Long interviewId, LocalDateTime scheduledTime) {
        validateInterviewIdAndScheduledTime(interviewId, scheduledTime);

        if (scheduledTime.isAfter(LocalDateTime.now())) {
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
    }

    public void scheduleInterviewReminderIfNotExists(Long interviewId, LocalDateTime startedAt) {
        validateInterviewIdAndScheduledTime(interviewId, startedAt);

        LocalDateTime oneDayBefore = startedAt.minusDays(1);
        if (oneDayBefore.isAfter(LocalDateTime.now())) {
            scheduleSingleReminderIfNotExists(interviewId, oneDayBefore, "D1");
        }

        LocalDateTime thirtyMinutesBefore = startedAt.minusMinutes(30);
        if (thirtyMinutesBefore.isAfter(LocalDateTime.now())) {
            scheduleSingleReminderIfNotExists(interviewId, thirtyMinutesBefore, "M30");
        }
    }

    public void scheduleSingleReminderIfNotExists(Long interviewId, LocalDateTime time, String type) {
        // scheduleSingleReminderIfNotExists를 사용하기 전 검증을 하고 호출함
        try {
            String jobName = String.format("interview-reminder-%s-job-%d", type, interviewId); //interview-reminder-M30-job-1
            String triggerName = String.format("interview-reminder-%s-trigger-%d", type, interviewId);

            JobDetail jobDetail = JobBuilder.newJob(InterviewReminderJob.class)
                    .withIdentity(jobName)
                    .usingJobData("interviewId", interviewId)
                    .usingJobData("reminderType", type)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(triggerName)
                    .startAt(Timestamp.valueOf(time))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule interview reminder job", e);
        }
    }


    public void cancelScheduledInterview(Long interviewId) {

        if (interviewId == null) {
            throw new IllegalArgumentException("Interview id cannot be null");
        }

        log.info("Canceling scheduled interview for interviewId: {}", interviewId);

        try {
            // 면접 시작 Job 삭제
            deleteJobAndTrigger("interview-start-job-" + interviewId, "interview-start-trigger-" + interviewId);

            // 1일 전 리마인더 삭제
            deleteJobAndTrigger("interview-reminder-D1-job-" + interviewId, "interview-reminder-D1-trigger-" + interviewId);

            // 30분 전 리마인더 삭제
            deleteJobAndTrigger("interview-reminder-M30-job-" + interviewId, "interview-reminder-M30-trigger-" + interviewId);

            log.info("Successfully canceled scheduled interview for interviewId: {}", interviewId);
        } catch (SchedulerException e) {
            log.error("Failed to cancel scheduled interview for interviewId: {}", interviewId, e);
            throw new RuntimeException("Failed to cancel scheduled interview for interviewId: " + interviewId, e);
        }
    }

    private void deleteJobAndTrigger(String jobName, String triggerName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName);
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);

        if (scheduler.checkExists(triggerKey)) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
        }

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }

    private void validateInterviewIdAndScheduledTime(Long interviewId, LocalDateTime scheduledTime) {
        if (interviewId == null || scheduledTime == null) {
            throw new IllegalArgumentException("Interview id and scheduled time cannot be null");
        }
    }
}