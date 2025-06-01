package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.domain.enums.InterviewType;
import cloudcomputinginha.demo.domain.enums.StartType;
import cloudcomputinginha.demo.domain.enums.VoiceType;
import cloudcomputinginha.demo.validation.annotation.ExistCoverletter;
import cloudcomputinginha.demo.validation.annotation.ExistResume;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewRequestDTO {
    @Getter
    public static class endInterviewRequestDTO {
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewCreateDTO {
        // 면접 정보
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        private String sessionName; // 그룹의 경우 필요
        @NotBlank
        private String corporateName; // 기업명
        @NotBlank
        private String jobName; // 직무명

        // 면접 옵션 정보
        @NotNull
        private InterviewFormat interviewFormat; //일대일 or 일대다
        @NotNull
        private InterviewType interviewType; // 기술 or 면접
        @NotNull
        private VoiceType voiceType; // 목소리 누구 할래?
        @NotNull
        private int questionNumber; // 질문 개수
        @NotNull
        private int answerTime; // 대답 시간

        // 시작 정보
        private StartType startType;
        private String scheduledDate;
        private String scheduledTime;

        // 인원 및 공개 설정 (그룹일때만)
        private Integer maxParticipants;
        private Boolean isOpen;

        // 이력서 & 자기소개서
        private Long resumeId;
        private String resumeTitle;
        private Long coverLetterId;
        private String coverLetterTitle;

        // 초대 이메일
        private List<InviteEmailDTO> inviteEmailDTOList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteEmailDTO {
        private Long id;
        private String email;
    }
}
