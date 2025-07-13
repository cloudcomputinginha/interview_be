package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewScheduleHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.scheduler.job.InterviewReminderJob;
import cloudcomputinginha.demo.scheduler.job.InterviewSchedulerJob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewScheduler {

    @Getter
    private final Scheduler scheduler;

    /**
     * interviewId에 대한 시작 Job이 이미 존재하지 않고,
     * scheduledTimes 현재 시간보다 나중이라면
     * 면접 시작 Job을 등록한다.
     *
     * @param interviewId   : 면접 id
     * @param scheduledTime : 면접 시작 시간
     */
    public void scheduleInterviewStart(Long interviewId, LocalDateTime scheduledTime) {
        validateInterviewIdAndScheduledTime(interviewId, scheduledTime);

        if (!isAlreadyScheduled(getInterviewStartJobName(interviewId)) && scheduledTime.isAfter(LocalDateTime.now())) {
            try {
                JobDetail jobDetail = JobBuilder.newJob(InterviewSchedulerJob.class)
                        .withIdentity(getInterviewStartJobName(interviewId))
                        .usingJobData("interviewId", interviewId)
                        .storeDurably()
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withIdentity(getInterviewStartTriggerName(interviewId))
                        .startAt(Timestamp.valueOf(scheduledTime))
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_SCHEDULE_CREATE_FAIL);
            }
        }
    }

    /**
     * interviewId에 대한 리마인더 Job이 이미 존재하지 않고,
     * 리마인드 예정 시간이 현재 시간보다 나중이라면
     * 면접 리마인드 Job을 등록한다.
     *
     * @param interviewId : 면접 id
     * @param startedAt   : 면접 시작 시간
     * @param before      : 면접 리마인더 알림 주기
     * @param type        : D1(하루 전)/M30(30분 전)
     */
    public void scheduleInterviewReminderIfNotExists(Long interviewId, LocalDateTime startedAt, Duration before, String type) {
        validateInterviewIdAndScheduledTime(interviewId, startedAt);

        LocalDateTime reminderTime = startedAt.minus(before);

        if (!isAlreadyScheduled(getReminderJobName(type, interviewId)) && reminderTime.isAfter(LocalDateTime.now())) {
            scheduleSingleReminder(interviewId, reminderTime, type);
        }
    }

    /**
     * 면접 리마인드 job을 등록하는 내부 로직이다.
     *
     * @param interviewId   : 면접 id
     * @param scheduledTime : job 실행 시간
     * @param type          : 리마인더 알림 종류
     */
    private void scheduleSingleReminder(Long interviewId, LocalDateTime scheduledTime, String type) {
        try {
            String jobName = getReminderJobName(type, interviewId);
            String triggerName = getReminderTriggerName(type, interviewId);

            JobDetail jobDetail = JobBuilder.newJob(InterviewReminderJob.class)
                    .withIdentity(jobName)
                    .usingJobData("interviewId", interviewId)
                    .usingJobData("reminderType", type)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(triggerName)
                    .startAt(Timestamp.valueOf(scheduledTime))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_REMINDER_CREATE_FAIL);
        }
    }

    /**
     * 면접에 해당하는 모든 스케쥴 작업(시작, 리마인더)을 취소한다.
     *
     * @param interviewId : 면접 id
     */
    public void cancelScheduledInterview(Long interviewId) {
        if (interviewId == null) {
            throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_SCHEDULE_INVALID_ARGUMENT);
        }

        log.info("Canceling scheduled interview for interviewId: {}", interviewId);

        try {
            deleteJobAndTrigger(getInterviewStartJobName(interviewId), getInterviewStartTriggerName(interviewId));
            deleteJobAndTrigger(getReminderJobName("D1", interviewId), getReminderTriggerName("D1", interviewId));
            deleteJobAndTrigger(getReminderJobName("M30", interviewId), getReminderTriggerName("M30", interviewId));

            log.info("Successfully canceled scheduled interview for interviewId: {}", interviewId);
        } catch (SchedulerException e) {
            log.error("Failed to cancel scheduled interview for interviewId: {}", interviewId, e);
            throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_SCHEDULE_CANCEL_FAIL);
        }
    }

    /**
     * 스케쥴러에 등록된 Job과 Trigger를 취소한다.
     *
     * @param jobName     : job 이름
     * @param triggerName : trigger 이름
     * @throws SchedulerException
     */
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

    /**
     * Quartz에 해당 Job이 이미 등록되었는지 확인한다.
     *
     * @param jobName : job 이름
     * @return 이미 등록되었으면 true, 등록되지 않았으면 false
     */
    private boolean isAlreadyScheduled(String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName);
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_SCHEDULE_CHECK_FAIL);
        }
    }

    /**
     * 인터뷰 id와 스케쥴 타임이 null이면 예외를 던진다.
     *
     * @param interviewId : 면접 id
     * @param time        : 시간
     */
    private void validateInterviewIdAndScheduledTime(Long interviewId, LocalDateTime time) {
        if (interviewId == null || time == null) {
            throw new InterviewScheduleHandler(ErrorStatus.INTERVIEW_SCHEDULE_INVALID_ARGUMENT);
        }
    }

    private String getInterviewStartJobName(Long interviewId) {
        return "interview-start-job-" + interviewId;
    }

    private String getInterviewStartTriggerName(Long interviewId) {
        return "interview-start-trigger-" + interviewId;
    }

    private String getReminderJobName(String type, Long interviewId) {
        return String.format("interview-reminder-%s-job-%d", type, interviewId);
    }

    private String getReminderTriggerName(String type, Long interviewId) {
        return String.format("interview-reminder-%s-trigger-%d", type, interviewId);
    }
}