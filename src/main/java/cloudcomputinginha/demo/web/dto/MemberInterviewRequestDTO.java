package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.validation.annotation.ExistCoverletter;
import cloudcomputinginha.demo.validation.annotation.ExistResume;
import cloudcomputinginha.demo.validation.annotation.ValidInterviewStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MemberInterviewRequestDTO {
    @Getter
    public static class changeMemberStatusDTO {
        @ValidInterviewStatus
        @NotEmpty
        private String status;

        public InterviewStatus getStatus() {
            return InterviewStatus.valueOf(status.toUpperCase());
        }
    }

    @Getter
    public static class createMemberInterviewDTO {
        @ExistResume
        @NotNull
        private Long resumeId;
        @ExistCoverletter
        @NotNull
        private Long coverletterId;
    }

    @Getter
    public static class updateDocumentDTO {
        // 기존 ID 값 -> 유지
        // 새로운 ID 값 - > 변경
        @ExistResume
        @NotNull
        private Long resumeId;
        @ExistCoverletter
        @NotNull
        private Long coverletterId;
    }
}
