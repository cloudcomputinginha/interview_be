package cloudcomputinginha.demo.web.controller;


import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.service.InterviewCommandService;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "인터뷰 API")
@RequiredArgsConstructor
public class InterviewRestController {
    private final InterviewCommandService interviewCommandService;

    @PatchMapping("/interviews/{interviewId}/end")
    @Operation(summary = "면접 종료 API", description = "면접 종료 시간과 사용자 면접의 상태를 변경합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewEndResultDTO> terminateInterview(@PathVariable @ExistInterview Long interviewId, @RequestBody @Valid InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewCommandService.terminateInterview(interviewId, endInterviewRequestDTO);
        return ApiResponse.onSuccess(InterviewConverter.toInterviewEndResultDTO(interview));
    }
}
