package cloudcomputinginha.demo.web.controller;


import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.service.interview.InterviewCommandService;
import cloudcomputinginha.demo.service.interview.InterviewQueryService;
import cloudcomputinginha.demo.service.interviewOption.InterviewOptionCommandService;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import cloudcomputinginha.demo.web.dto.InterviewOptionRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "인터뷰 API")
@RequiredArgsConstructor
@RequestMapping("/interviews")
@Validated
public class InterviewRestController {
    private final InterviewCommandService interviewCommandService;
    private final InterviewQueryService interviewQueryService;
    private final InterviewOptionCommandService interviewOptionCommandService;

    @PatchMapping("/{interviewId}/end")
    @Operation(summary = "면접 종료 API", description = "면접 종료 시간과 사용자 면접의 상태를 변경합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewEndResultDTO> terminateInterview(@AuthenticationPrincipal Long memberId, @PathVariable @ExistInterview @NotNull Long interviewId, @RequestBody @Valid InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewCommandService.terminateInterview(memberId, interviewId, endInterviewRequestDTO);
        return ApiResponse.onSuccess(InterviewConverter.toInterviewEndResultDTO(interview));
    }

    @PostMapping
    @Operation(summary = "면접 생성 API", description = "새로운 면접을 생성합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewCreateResultDTO> createInterview(@AuthenticationPrincipal Long memberId, @RequestBody @Valid InterviewRequestDTO.InterviewCreateDTO request) {
        InterviewResponseDTO.InterviewCreateResultDTO result = interviewCommandService.createInterview(request, memberId);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/group")
    @Operation(summary = "일대다 면접 모집글 조회 API", description = "일대다 면접 모집글을 조회합니다.")
    public ApiResponse<List<InterviewResponseDTO.InterviewGroupCardDTO>> getGroupInterviewCards(@AuthenticationPrincipal Long memberId) {
        List<InterviewResponseDTO.InterviewGroupCardDTO> result = interviewQueryService.getGroupInterviewCards(memberId);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{interviewId}/start")
    @Operation(summary = "면접 시작 API", description = "해당 API가 호출되면 AI서버에게 넘겨줄 면접의 모든 정보를 넘겨줍니다.")
    public ApiResponse<InterviewResponseDTO.InterviewStartResponseDTO> startInterview(@AuthenticationPrincipal Long memberId, @PathVariable @NotNull Long interviewId) {
        InterviewResponseDTO.InterviewStartResponseDTO interviewStartResponse = interviewCommandService.startInterview(memberId, interviewId, false);
        return ApiResponse.onSuccess(interviewStartResponse);
    }

    @GetMapping("/group/{interviewId}")
    @Operation(summary = "일대다 면접 모집글 세부 조회 API", description = "일대다 면접 모집글 세부를 조회합니다.")
    public ApiResponse<InterviewResponseDTO.GroupInterviewDetailDTO> getGroupInterviewDetail(@AuthenticationPrincipal Long memberId, @PathVariable @NotNull @ExistInterview Long interviewId) {
        return ApiResponse.onSuccess(interviewQueryService.getGroupInterviewDetail(memberId, interviewId));
    }

    @PatchMapping("/{interviewId}")
    @Operation(summary = "면접 수정 API", description = "면접 이름, 설명, 최대 인원, 공개 여부를 수정합니다.")
    public ApiResponse<InterviewResponseDTO.InterviewUpdateResponseDTO> updateInterview(@AuthenticationPrincipal Long memberId, @PathVariable Long interviewId, @RequestBody @Valid InterviewRequestDTO.InterviewUpdateDTO request) {
        InterviewResponseDTO.InterviewUpdateResponseDTO result = interviewCommandService.updateInterview(memberId, interviewId, request);
        return ApiResponse.onSuccess(result);
    }

    @PatchMapping("/{interviewId}/option")
    @Operation(summary = "면접 옵션 수정 API", description = "목소리 유형, 최대 질문 개수, 답변 가능 시간을 수정합니다.")
    public ApiResponse<InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO> updateInterviewOption(@AuthenticationPrincipal Long memberId, @PathVariable Long interviewId, @RequestBody @Valid InterviewOptionRequestDTO.InterviewOptionUpdateDTO request) {
        InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO result = interviewOptionCommandService.updateInterviewOption(memberId, interviewId, request);
        return ApiResponse.onSuccess(result);
    }
}
