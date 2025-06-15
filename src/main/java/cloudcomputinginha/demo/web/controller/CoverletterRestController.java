package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.service.coverletter.CoverletterCommandService;
import cloudcomputinginha.demo.service.coverletter.CoverletterQueryService;
import cloudcomputinginha.demo.service.qna.QnaQueryService;
import cloudcomputinginha.demo.validation.annotation.ExistCoverletter;
import cloudcomputinginha.demo.validation.annotation.ExistMember;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "자기소개서 API")
@RestController
@RequestMapping("/coverletters")
@RequiredArgsConstructor
@Validated
public class CoverletterRestController {
    private final CoverletterCommandService coverletterCommandService;
    private final CoverletterQueryService coverletterQueryService;
    private final QnaQueryService qnaQueryService;
    private final MemberRepository memberRepository;

    @PostMapping
    @Operation(summary = "자기소개서 생성")
    public ApiResponse<CoverletterResponseDTO.createCoverletterDTO> createCoverletter(@AuthenticationPrincipal Long memberId, @RequestBody @Valid CoverletterRequestDTO.createCoverletterDTO createCoverletterDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Coverletter coverletter = coverletterCommandService.saveCoverletter(memberId, createCoverletterDTO);
        return ApiResponse.onSuccess(CoverletterConverter.toCreateCoverletterResponseDTO(coverletter));
    }

    @GetMapping("/me")
    @Operation(summary = "나의 자기소개서 리스트 조회")
    public ApiResponse<CoverletterResponseDTO.MyCoverletterListDTO> findMyCoverletter(@AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Coverletter> coverletterList = coverletterQueryService.findAllCoverletterByMember(memberId);
        return ApiResponse.onSuccess(CoverletterConverter.toMyCoverletterListDTO(coverletterList));
    }

    @GetMapping("/{coverletterId}")
    @Operation(summary = "자기소개서 세부 조회")
    public ApiResponse<CoverletterResponseDTO.CoverletterDetailDTO> getCoverletterDetail(@AuthenticationPrincipal Long memberId, @PathVariable @ExistCoverletter @NotNull Long coverletterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Coverletter coverletter = coverletterQueryService.getCoverletter(coverletterId);
        coverletter.validateOwner(memberId);
        List<Qna> qnaList = qnaQueryService.getQnasByCoverletter(coverletterId);

        return ApiResponse.onSuccess(CoverletterConverter.toDetailDTO(coverletter, qnaList));
    }

}

