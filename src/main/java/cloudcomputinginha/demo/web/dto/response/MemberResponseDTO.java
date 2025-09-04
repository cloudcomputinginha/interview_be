package cloudcomputinginha.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoReponseDTO {
        private Long memberId;
        private String name;
        private String email;
        private String phone;
        private String jobType;
        private String introduction;
    }
}
