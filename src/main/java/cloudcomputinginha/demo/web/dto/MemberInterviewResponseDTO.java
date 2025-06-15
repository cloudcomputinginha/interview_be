package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MemberInterviewResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ParticipantDTO {
        private Long memberInterviewId;
        private ResumeResponseDTO.ResumeSimpleDTO resumeDTO;
        private CoverletterResponseDTO.CoverletterDetailDTO coverLetterDTO;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberInterviewStatusDTO {
        Long memberInterviewId;
        InterviewStatus status;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateMemberInterviewDTO {
        Long memberInterviewId;
        LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyInterviewListDTO {
        List<MyInterviewDTO> myInterviews;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyInterviewDTO {
        /*
        - 인터뷰
            - 인터뷰 id, 이름, 회사, 직무명, 현재 인원, 최대 인원, 면접 예정 시각, 종료 시각 //InterviewCardDTO
        - 인터뷰 옵션
            - 인터뷰 포멧, 인터뷰 타입 //InterviewOptionPreviewDTO
        - 멤버 인터뷰
            - 멤버 인터뷰 id, 상태 //ParticipantDTO
         */
        private InterviewResponseDTO.InterviewCardDTO myInterviewCardDTO;
        private InterviewOptionResponseDTO.InterviewOptionPreviewDTO interviewOptionPreviewDTO;
        private MemberInterviewStatusDTO memberInterviewStatusDTO;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberInterviewDocumentDTO {
        private Long memberInterviewId;
        private Long resumeId;
        private Long coverLetterId;
        private LocalDateTime updatedAt;
    }
}
