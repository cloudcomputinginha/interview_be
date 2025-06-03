package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.domain.enums.InterviewType;
import cloudcomputinginha.demo.domain.enums.StartType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewEndResultDTO {
        private Long interviewId;
        private LocalDateTime endedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewCreateResultDTO {
        private Long interviewId;
        private String name; // 면접 이름
        private String description; // 면접 설명
        private String corporateName; // 기업명
        private String jobName; // 직무명
        private InterviewFormat interviewFormat; // 면접 유형
        private InterviewType interviewType; // 기술 or 인성
        private StartType startType; // 즉시 or 예정
        private LocalDateTime startedAt; // 면접 시작 시간
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewGroupCardDTO {
        private Long interviewId;
        private String name;
        private String description;
        private String sessionName;
        private String jobName;
        private InterviewType interviewType;
        private int currentParticipants;
        private int maxParticipants;
        private LocalDateTime startedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewStartResponseDTO {
        private Long interviewId;
        private InterviewResponseDTO.InterviewDTO interview;
        private InterviewOptionResponseDTO.InterviewOptionDetailDTO options;
        private List<MemberInterviewResponseDTO.ParticipantDTO> participants;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewDTO {
        private Long interviewId;
        private String corporateName;
        private String jobName;
        private StartType startType;
        private int participantCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupInterviewDetailDTO {
        private Long interviewId;
        private String name;
        private String description;
        private String sessionName;
        private String jobName;

        private InterviewType interviewType;
        private int maxParticipants;
        private int currentParticipants;
        private LocalDateTime startedAt;

        private String hostName;
        private List<GroupInterviewParticipantDTO> groupInterviewParticipants;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupInterviewParticipantDTO {
        private Long memberId; // 참가자 id
        private String name; // 그룹 면접 참가자 이름
        private boolean isHost; // 참가자가 호스트인지 여부
        private boolean isSubmitted; // 참가자가 자료를 제출했는지 여부
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewUpdateResponseDTO {
        private Long interviewId;
        private String name;
        private String description;
        private Integer maxParticipants;
        private boolean isOpen;
    }
}
