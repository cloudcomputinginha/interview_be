package cloudcomputinginha.demo.web.controller;


import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.service.InterviewCommandService;
import cloudcomputinginha.demo.service.InterviewQueryService;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "인터뷰 API")
@RequiredArgsConstructor
@RequestMapping("/interviews")
public class InterviewRestController {
    private final InterviewCommandService interviewCommandService;
    private final InterviewQueryService interviewQueryService;

    @PatchMapping("/{interviewId}/end")
    @Operation(summary = "면접 종료 API", description = "면접 종료 시간과 사용자 면접의 상태를 변경합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewEndResultDTO> terminateInterview(@PathVariable @ExistInterview Long interviewId, @RequestBody @Valid InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewCommandService.terminateInterview(interviewId, endInterviewRequestDTO);
        return ApiResponse.onSuccess(InterviewConverter.toInterviewEndResultDTO(interview));
    }

    @PostMapping
    @Operation(summary = "면접 생성 API", description = "새로운 면접을 생성합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewCreateResultDTO> createInterview(@RequestBody @Valid InterviewRequestDTO.InterviewCreateDTO request) {
        Long memberId = 2L;
        InterviewResponseDTO.InterviewCreateResultDTO result = interviewCommandService.createInterview(request, memberId);
        return ApiResponse.onSuccess(result);
    }
  
    @GetMapping("/interviews/group")
    @Operation(summary = "일대다 면접 모집글 조회 API", description = "일대다 면접 모집글을 조회합니다.")
    public ApiResponse<List<InterviewResponseDTO.InterviewGroupCardDTO>> getGroupInterviewCards() {
        List<InterviewResponseDTO.InterviewGroupCardDTO> result = interviewQueryService.getGroupInterviewCards();
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{interviewId}/start")
    @Operation(summary = "면접 시작 API", description = "해당 API가 호출되면 AI서버에게 넘겨줄 면접의 모든 정보를 넘겨줍니다.")
    public ApiResponse<InterviewResponseDTO.InterviewStartResponseDTO> startInterview(@PathVariable @NotNull @ExistInterview Long interviewId) {
        InterviewResponseDTO.InterviewStartResponseDTO interviewStartResponse = interviewCommandService.startInterview(interviewId);
        return ApiResponse.onSuccess(interviewStartResponse);
    }
}
