package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.service.interview.InterviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewScheduleInitializer {

    private final InterviewQueryService interviewQueryService;
    private final InterviewScheduler interviewScheduler;

    /**
     * 서버가 시작될 때(ApplicationReadyEvent), 앞으로 예정된 면접 일정에 대해 면접 시작 및 리마인더 알림을 예약한다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeScheduledInterviews() {
        List<Interview> upcoming = interviewQueryService.getUpcomingInterviews(LocalDateTime.now());
        for (Interview interview : upcoming) {
            Long interviewId = interview.getId();
            LocalDateTime startedAt = interview.getStartedAt();

            interviewScheduler.scheduleInterviewStart(interviewId, startedAt);

            interviewScheduler.scheduleInterviewReminderIfNotExists(interviewId, startedAt, Duration.ofDays(1), "D1");

            interviewScheduler.scheduleInterviewReminderIfNotExists(interviewId, startedAt, Duration.ofMinutes(30), "M30");
        }
    }
}
