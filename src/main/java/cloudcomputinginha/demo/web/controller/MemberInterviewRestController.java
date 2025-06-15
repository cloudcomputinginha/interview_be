package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.service.memberInterview.MemberInterviewCommandService;
import cloudcomputinginha.demo.service.memberInterview.MemberInterviewQueryService;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 인증 토큰 기반
@Validated
@RestController
@Tag(name = "사용자 인터뷰 API")
@RequiredArgsConstructor
public class MemberInterviewRestController {
    private final MemberInterviewCommandService memberInterviewCommandService;
    private final MemberInterviewQueryService memberInterviewQueryService;

    @Deprecated
    @PatchMapping("/interviews/{interviewId}/waiting-room")
    @Operation(summary = "면접 대기실 내 사용자의 상태를 변경하는 API", description = "면접, 사용자 id, 사용자 상태를 요청 받아 사용자 면접의 상태를 수정합니다.")
    public ApiResponse<MemberInterviewResponseDTO.MemberInterviewStatusDTO> changeParticipantsStatus(
            @AuthenticationPrincipal Long memberId,
            @PathVariable @ExistInterview @NotNull Long interviewId,
            @RequestBody @Valid MemberInterviewRequestDTO.changeMemberStatusDTO changeMemberStatusDTO) {
        MemberInterview memberInterview = memberInterviewCommandService.changeMemberInterviewStatus(interviewId, memberId, changeMemberStatusDTO.getStatus());
        return ApiResponse.onSuccess(MemberInterviewConverter.toMemberInterviewStatusDTO(memberInterview));
    }

    @PostMapping("/interviews/{interviewId}")
    @Operation(summary = "일대다 면접 참여 신청 API", description = "면접, 사용자, 이력서, 자소서 id를 전부 받아와 사용자 면접을 생성합니다.")
    public ApiResponse<MemberInterviewResponseDTO.CreateMemberInterviewDTO> createMemberInterview(
            @AuthenticationPrincipal Long memberId,
            @PathVariable @ExistInterview @NotNull Long interviewId,
            @RequestBody @Valid MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO) {
        MemberInterview memberInterview = memberInterviewCommandService.createMemberInterview(interviewId, memberId, createMemberInterviewDTO);
        return ApiResponse.onSuccess(MemberInterviewConverter.toMemberInterviewResultDTO(memberInterview));
    }

    @GetMapping("/mypage/interviews")
    @Operation(summary = "나의 면접 리스트 조회 API", description = "나의 면접 리스트를 조회합니다.(상태 구분, 페이징 X)")
    public ApiResponse<MemberInterviewResponseDTO.MyInterviewListDTO> getMyInterviews(@AuthenticationPrincipal Long memberId) {
        List<MemberInterview> myInterviews = memberInterviewQueryService.getMyInterviews(memberId);
        return ApiResponse.onSuccess(MemberInterviewConverter.toMyInterviewListDTO(myInterviews));
    }

    @PatchMapping("/interviews/{interviewId}/documents")
    @Operation(summary = "나의 면접에서 자기소개서 또는 이력서 변경 API", description = "면접 시작전, 자기소개서나 이력서를 변경(변경할 ID) 합니다. 만약 변경하지 않을 경우 기존 아이디를 담아서 요청해주세요!")
    public ApiResponse<MemberInterviewResponseDTO.MemberInterviewDocumentDTO> updateInterviewDocuments(
            @AuthenticationPrincipal Long memberId,
            @PathVariable @NotNull @ExistInterview Long interviewId,
            @RequestBody @Valid MemberInterviewRequestDTO.updateDocumentDTO updateDocumentDTO) {
        MemberInterview memberInterview = memberInterviewCommandService.changeMemberInterviewDocument(interviewId, memberId, updateDocumentDTO);
        return ApiResponse.onSuccess(MemberInterviewConverter.toMemberInterviewDocumentDTO(memberInterview));
    }
}
