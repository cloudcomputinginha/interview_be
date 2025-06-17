package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.service.member.MemberCommandService;
import cloudcomputinginha.demo.web.dto.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "사용자 API")
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {
    private final MemberCommandService memberCommandService;

    @PatchMapping("")
    @Operation(summary = "사용자 기본 정보 등록 API", description = "사용자의 기본 정보(전화번호, 직무 분야, 자기소개)를 등록합니다.")
    public ApiResponse<MemberInfoResponseDTO> registerBasicInfo(@AuthenticationPrincipal Long memberId, @RequestBody @Valid MemberInfoRequestDTO.registerInfoDTO request) {
        MemberInfoResponseDTO result = memberCommandService.registerBasicInfo(memberId, request);
        return ApiResponse.onSuccess(result);
    }

    @PatchMapping("/info")
    @Operation(summary = "사용자 기본 정보 변경 API", description = "사용자의 기본 정보(이름, 전화번호, 직무 분야, 자기소개)를 변경합니다.")
    public ApiResponse<MemberInfoResponseDTO> updateBasicInfo(@AuthenticationPrincipal Long memberId, @RequestBody @Valid MemberInfoRequestDTO.updateInfoDTO request) {
        MemberInfoResponseDTO result = memberCommandService.updateBasicInfo(memberId, request);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping
    @Operation(summary = "사용자 프로필 조회 API", description = "사용자의 프로필을 조회합니다.")
    public ApiResponse<MemberInfoResponseDTO> getBasicInfo(@AuthenticationPrincipal Long memberId) {
        MemberInfoResponseDTO result = memberCommandService.getBasicInfo(memberId);
        return ApiResponse.onSuccess(result);
    }
}
