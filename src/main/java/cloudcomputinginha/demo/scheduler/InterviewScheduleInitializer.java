package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewScheduleInitializer {

    private final InterviewRepository interviewRepository;
    private final InterviewScheduler interviewScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeScheduledInterviews() {
        List<Interview> upcoming = interviewRepository.findAllByStartedAtAfterAndEndedAtIsNull(LocalDateTime.now());
        System.out.println("upcoming = " + upcoming.size());

        for (Interview interview : upcoming) {
            Long interviewId = interview.getId();

            if (!isAlreadyScheduled("interview-start-job-" + interviewId)) {
                // 면접 시작 스케쥴
                interviewScheduler.scheduleInterviewStart(interview.getId(), interview.getStartedAt());
            }

            // 리마인더 스케줄- 1일 전
            LocalDateTime oneDayBefore = interview.getStartedAt().minusDays(1);
            if (oneDayBefore.isAfter(LocalDateTime.now()) && !isAlreadyScheduled("interview-reminder-D1-job-" + interviewId)) {
                interviewScheduler.scheduleSingleReminderIfNotExists(interviewId, oneDayBefore, "D1");
            }

            // 리마인더 스케줄 - 30분 전
            LocalDateTime thirtyMinutesBefore = interview.getStartedAt().minusMinutes(30);
            if (thirtyMinutesBefore.isAfter(LocalDateTime.now()) && !isAlreadyScheduled("interview-reminder-M30-job-" + interviewId)) {
                interviewScheduler.scheduleSingleReminderIfNotExists(interviewId, thirtyMinutesBefore, "M30");
            }
        }
    }

    private boolean isAlreadyScheduled(String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName);
            return interviewScheduler.getScheduler().checkExists(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to check job existence", e);
        }
    }

}