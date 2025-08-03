package cloudcomputinginha.demo.infra.ai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AI 서버와 외부 통신
 */
@FeignClient(name = "AiOpenFeign", url = "${domain.interview-ai}")
public interface AiOpenFeignService {
    @GetMapping("/interview/ocr")
    String getInterviewOcrResult(@RequestParam(value = "pdf_path") String fileUrl);
}
