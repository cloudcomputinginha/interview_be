package cloudcomputinginha.demo.config;

import cloudcomputinginha.demo.infra.ai.AiServiceErrorDecoder;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 클라이언트 설정
 * - 타임아웃 설정
 * - 에러 디코더 설정
 * - 재시도 설정
 */
@Configuration
@RequiredArgsConstructor
public class AiFeignConfig {

    private final AiServiceErrorDecoder customErrorDecoder;

    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(5000, 15000);
        // 5s 연결 타임아웃, 15s 읽기(응답대기) 타임아웃
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return customErrorDecoder;
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}