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

            // ✅ 면접 시작 스케줄
            if (!isAlreadyScheduled("interview-start-job-" + interviewId)) {
                interviewScheduler.scheduleInterviewStart(interviewId, interview.getStartedAt());
            }

            // ✅ 리마인더 - 1일 전
            LocalDateTime oneDayBefore = interview.getStartedAt().minusDays(1);
            String jobD1 = generateReminderJobName("D1", interviewId);
            if (oneDayBefore.isAfter(LocalDateTime.now()) && !isAlreadyScheduled(jobD1)) {
                interviewScheduler.scheduleSingleReminderIfNotExists(interviewId, oneDayBefore, "D1");
            }

            // ✅ 리마인더 - 30분 전
            LocalDateTime thirtyMinutesBefore = interview.getStartedAt().minusMinutes(30);
            String jobM30 = generateReminderJobName("M30", interviewId);
            if (thirtyMinutesBefore.isAfter(LocalDateTime.now()) && !isAlreadyScheduled(jobM30)) {
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

    private String generateReminderJobName(String type, Long interviewId) {
        return String.format("interview-reminder-%s-job-%d", type, interviewId);
    }

}