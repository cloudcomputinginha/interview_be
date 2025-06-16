package cloudcomputinginha.demo.domain.enums;

import cloudcomputinginha.demo.config.DomainInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    ROOM_ENTRY(
            "%s님이 %s 모의면접 방에 입장하셨습니다.", //member.getName(), interview.getName()
            "/interviews/group/%s" //interview.getId()
    ), // 모의면접 방 참가 알림
    //ROOM_INVITE // 모의면접 방 초대 알림
    INTERVIEW_REMINDER_1D(
            "1일 후 %s 모의면접 시작 예정입니다.", //interview.getName()
            "/interviews/group/%s" //interview.getId()
    ), //모의면접 1일 전 리마인드
    INTERVIEW_REMINDER_30M(
            "30분 후 %s 모의면접 시작 예정입니다.", //interview.getName()
            "/interviews/group/%s" //interview.getId()
    ), //모의면접 30분 전 리마인드
    FEEDBACK_RECEIVED(
            "% 모의면접 AI 피드백이 도착했습니다.", //interview.getName()
            "/interview/session/%s/%s"
    ); //비동기 피드백 도착 알림

    private final String messageTemplate;
    private final String urlTemplate;

    public String generateMessage(Object... args) {
        return String.format(messageTemplate, args);
    }

    public String generateUrl(Object... args) {
        String requestDomain = switch (this) {
            case FEEDBACK_RECEIVED -> DomainInfo.INTERVIEW_AI;
            default -> DomainInfo.INTERVIEW;
        };
        return requestDomain + String.format(urlTemplate, args);
    }
}
