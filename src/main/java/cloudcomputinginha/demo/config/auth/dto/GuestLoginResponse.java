package cloudcomputinginha.demo.config.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestLoginResponse {
    private String accessToken;
    private Long memberId;
    private String randomName;
}
