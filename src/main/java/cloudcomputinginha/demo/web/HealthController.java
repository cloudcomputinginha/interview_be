package cloudcomputinginha.demo.web;


import cloudcomputinginha.demo.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/healthz")
    public ApiResponse<Void> health() {
        return ApiResponse.onSuccess(null);
    }
}

