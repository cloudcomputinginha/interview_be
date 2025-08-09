package cloudcomputinginha.demo.preprocessor;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.config.properties.DomainProperties;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.service.notification.NotificationCommandService;
import cloudcomputinginha.demo.web.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationPreprocessor {
    private final MemberInterviewRepository memberInterviewRepository;
    private final NotificationCommandService notificationCommandService;
    private final DomainProperties domainProperties;

    @Transactional // 트랜잭션 범위를 명확하게 하기 위해 붙였습니다 TODO: 한 번 트랜잭션 전파 관련해서 공부해야 할 것 같아요
    public void preprocessFeedbackDTO(NotificationRequestDTO.FeedbackArrivedDTO feedBackArrivedDTO) {
        MemberInterview memberInterview = memberInterviewRepository.findWithMemberAndInterviewByIdAndInterviewId(feedBackArrivedDTO.getMemberInterviewId(), feedBackArrivedDTO.getInterviewId())
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND)); // memberInterview 속 interviewId가 맞는지 한 번에 검증한다.
        if (memberInterview.getStatus() != InterviewStatus.DONE) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_STATUS_INVALID);
        }
        Member member = memberInterview.getMember();
        Interview interview = memberInterview.getInterview();

        NotificationType notificationType = NotificationType.FEEDBACK_RECEIVED;
        String message = notificationType.generateMessage(interview.getName());
        String url = notificationType.generateUrl(domainProperties, interview.getId(), memberInterview.getId());

        notificationCommandService.createNotificationAndSend(member, notificationType, message, url);
    }
}
