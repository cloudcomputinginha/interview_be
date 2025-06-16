package cloudcomputinginha.demo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDTO {
    private Long memberId;
    private String name;
    private String email;
    private String phone;
    private String jobType;
    private String introduction;
}
