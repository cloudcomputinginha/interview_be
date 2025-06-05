package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.repository.InterviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewScheduleInitializer {

    private final InterviewRepository interviewRepository;
    private final InterviewScheduler interviewScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeScheduledInterviews() {
        List<Interview> upcoming = interviewRepository.findAllByStartedAtAfter(LocalDateTime.now());

        for (Interview interview : upcoming) {
            if (!isAlreadyScheduled(interview.getId())) {
                interviewScheduler.scheduleInterviewStart(interview.getId(), interview.getStartedAt());
            }
        }
    }

    private boolean isAlreadyScheduled(Long interviewId) {
        try {
            JobKey jobKey = JobKey.jobKey("interview-start-job-" + interviewId);
            return interviewScheduler.getScheduler().checkExists(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to check job existence", e);
        }
    }

}