package cloudcomputinginha.demo.config.auth.service;

import cloudcomputinginha.demo.config.auth.domain.GoogleUser;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleUserInfoClient {

    public GoogleUser getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Authorization: Bearer <accessToken>
        HttpEntity<String> request = new HttpEntity<>(headers);

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GoogleUser> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                request,
                GoogleUser.class
        );

        // 응답 결과
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // 사용자 정보 객체 리턴
        } else {
            throw new RuntimeException("구글 사용자 정보 요청 실패: " + response.getStatusCode());
        }
    }
}
