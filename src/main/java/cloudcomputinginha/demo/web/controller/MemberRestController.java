package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.service.member.MemberCommandService;
import cloudcomputinginha.demo.web.dto.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "사용자 API")
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {
    private final MemberCommandService memberCommandService;

    @PatchMapping("/info")
    @Operation(summary = "사용자 기본 정보 등록 API", description = "사용자의 기본 정보(전화번호, 직무 분야, 자기소개)를 등록합니다.")
    public ApiResponse<MemberInfoResponseDTO> updateBasicInfo(@AuthenticationPrincipal Long memberId, @RequestBody MemberInfoRequestDTO request) {
        MemberInfoResponseDTO result = memberCommandService.updateBasicInfo(memberId, request);
        return ApiResponse.onSuccess(result);
    }
}
