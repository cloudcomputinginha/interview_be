package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.service.MemberInterviewCommandService;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "사용자 인터뷰 API")
@RequiredArgsConstructor
public class MemberInterviewRestController {
    private final MemberInterviewCommandService memberInterviewCommandService;

    @PatchMapping("/interviews/{interviewId}/waiting-room")
    @Operation(summary = "면접 대기실 내 사용자의 상태를 변경하는 API", description = "면접, 사용자 id, 사용자 상태를 요청 받아 사용자 면접의 상태를 수정합니다.")
    public ApiResponse<MemberInterviewResponseDTO.MemberInterviewStatusDTO> changeParticipantsStatus(@PathVariable @ExistInterview Long interviewId, @RequestBody @Valid MemberInterviewRequestDTO.changeMemberStatusDTO changeMemberStatusDTO) {
        MemberInterview memberInterview = memberInterviewCommandService.changeMemberInterviewStatus(interviewId, changeMemberStatusDTO);
        return ApiResponse.onSuccess(MemberInterviewConverter.toMemberInterviewStatusDTO(memberInterview));
    }

    @PostMapping("/interviews/{interviewId}")
    @Operation(summary = "면접 참여 신청 API", description = "면접, 사용자, 이력서, 자소서 id를 전부 받아와 사용자 면접을 생성합니다.")
    public ApiResponse<MemberInterviewResponseDTO.CreateMemberInterviewDTO> createMemberInterview(@PathVariable @ExistInterview Long interviewId, @RequestBody @Valid MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO) {
        MemberInterview memberInterview = memberInterviewCommandService.createMemberInterview(interviewId, createMemberInterviewDTO);
        return ApiResponse.onSuccess(MemberInterviewConverter.toMemberInterviewResultDTO(memberInterview));
    }

}
