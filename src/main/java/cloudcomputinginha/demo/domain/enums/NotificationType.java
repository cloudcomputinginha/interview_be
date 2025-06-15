package cloudcomputinginha.demo.domain.enums;

public enum NotificationType {
    ROOM_ENTRY, // 모의면접 방 참가 알림
    ROOM_INVITE, // 모의면접 방 초대 알림
    INTERVIEW_REMINDER_1D, //모의면접 1일 전 리마인드
    INTERVIEW_REMINDER_30M, //모의면접 30분 전 리마인드
    FEEDBACK_RECEIVED //비동기 피드백 도착 알림
}
