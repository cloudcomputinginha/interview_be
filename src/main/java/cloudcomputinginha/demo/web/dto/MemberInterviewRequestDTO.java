package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.validation.annotation.ExistMember;
import cloudcomputinginha.demo.validation.annotation.ValidInterviewStatus;
import lombok.Getter;

public class MemberInterviewRequestDTO {
    @Getter
    public static class changeMemberStatusDTO {
        @ExistMember
        private Long memberId;
        @ValidInterviewStatus
        private String status;

        public InterviewStatus getStatus() {
            return InterviewStatus.valueOf(status.toUpperCase());
        }
    }
}
