package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.domain.enums.InterviewType;
import cloudcomputinginha.demo.domain.enums.StartType;
import lombok.*;

import java.time.LocalDateTime;

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
}
