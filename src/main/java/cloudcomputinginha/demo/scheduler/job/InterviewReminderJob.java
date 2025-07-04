package cloudcomputinginha.demo.scheduler.job;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.service.interview.InterviewQueryService;
import cloudcomputinginha.demo.service.notification.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewReminderJob extends QuartzJobBean {
    private final NotificationCommandService notificationCommandService;
    private final InterviewQueryService interviewQueryService;
    private final MemberInterviewRepository memberInterviewRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Long interviewId = dataMap.getLong("interviewId");
        String type = dataMap.getString("reminderType"); // D1 or M30

        Interview interview = interviewQueryService.getInterview(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        List<MemberInterview> memberInterviews = memberInterviewRepository.findWithMemberByInterviewId(interviewId);

        NotificationType notificationType = switch (type) {
            case "D1" -> NotificationType.INTERVIEW_REMINDER_1D;
            case "M30" -> NotificationType.INTERVIEW_REMINDER_30M;
            default -> throw new NotificationHandler(ErrorStatus.NOTIFICATION_TYPE_INVALID);
        };

        String message = notificationType.generateMessage(interview.getName());

        String url = notificationType.generateUrl(interview.getId());

        memberInterviews.stream()
                .map(MemberInterview::getMember)
                .forEach(receiver ->
                        notificationCommandService.createNotificationAndSend(
                                receiver,
                                notificationType,
                                message,
                                url
                        )
                );

    }
}
