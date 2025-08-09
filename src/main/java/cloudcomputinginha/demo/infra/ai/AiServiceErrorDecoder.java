package cloudcomputinginha.demo.infra.ai;

import cloudcomputinginha.demo.apiPayload.code.handler.AiHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * AI Feign Client가 4xx, 5xx 에러를 받았을 떄 이를 처리하는 디코더
 */
@Component
public class AiServiceErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());

        // 4xx 에러 처리
        if (status.is4xxClientError()) {
            if (status == HttpStatus.NOT_FOUND) {
                return new AiHandler(ErrorStatus.AI_RESOURCE_NOT_FOUND);
            }
            return new AiHandler(ErrorStatus.AI_BAD_REQUEST);
        }

        // 5xx 에러 처리
        if (status.is5xxServerError()) {
            return new AiHandler(ErrorStatus.AI_INTERNAL_SERVER_ERROR);
        }

        return defaultDecoder.decode(s, response);
    }
}
