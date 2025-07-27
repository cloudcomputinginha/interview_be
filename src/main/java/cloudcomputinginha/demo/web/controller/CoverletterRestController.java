package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.service.coverletter.CoverletterCommandService;
import cloudcomputinginha.demo.service.coverletter.CoverletterQueryService;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @PostMapping
    @Operation(summary = "자기소개서 생성")
    public ApiResponse<CoverletterResponseDTO.createCoverletterDTO> createCoverletter(@AuthenticationPrincipal Long memberId, @RequestBody @Valid CoverletterRequestDTO.createCoverletterDTO createCoverletterDTO) {
        Coverletter coverletter = coverletterCommandService.saveCoverletter(memberId, createCoverletterDTO);
        return ApiResponse.onSuccess(CoverletterConverter.toCreateCoverletterResponseDTO(coverletter));
    }

    @GetMapping("/me")
    @Operation(summary = "나의 자기소개서 리스트 조회")
    public ApiResponse<CoverletterResponseDTO.MyCoverletterListDTO> findMyCoverletter(@AuthenticationPrincipal Long memberId) {
        List<Coverletter> coverletterList = coverletterQueryService.findAllCoverletterByMember(memberId);
        return ApiResponse.onSuccess(CoverletterConverter.toMyCoverletterListDTO(coverletterList));
    }

    @GetMapping("/{coverletterId}")
    @Operation(summary = "자기소개서 세부 조회")
    public ApiResponse<CoverletterResponseDTO.CoverletterDetailDTO> getCoverletterDetail(@AuthenticationPrincipal Long memberId, @PathVariable Long coverletterId) {
        Coverletter coverletter = coverletterQueryService.getOwnedCoverletter(coverletterId, memberId);

        return ApiResponse.onSuccess(CoverletterConverter.toDetailDTO(coverletter));
    }

    @GetMapping("/{coverletterId}/interviews")
    @Operation(summary = "해당 자기소개서를 사용중인 인터뷰 리스트 조회")
    public ApiResponse<List<InterviewResponseDTO.InterviewGroupCardDTO>> getResumeInterviews(
            @PathVariable Long coverletterId,
            @AuthenticationPrincipal Long memberId
    ) {
        Coverletter coverletter = coverletterQueryService.getOwnedCoverletter(coverletterId, memberId);

        List<Interview> interviews = coverletterQueryService.getInterviewsByCoverletter(coverletterId);
        List<InterviewResponseDTO.InterviewGroupCardDTO> interviewCards = interviews.stream()
                .map(InterviewConverter::toInterviewGroupCardDTO)
                .toList();
        return ApiResponse.onSuccess(interviewCards);
    }

    @DeleteMapping("/{coverletterId}")
    @Operation(summary = "이력서 삭제")
    public ApiResponse<Void> deleteResume(
            @PathVariable Long coverletterId,
            @AuthenticationPrincipal Long memberId
    ) {
        coverletterCommandService.deleteCoverletter(coverletterId, memberId);
        return ApiResponse.onSuccess(null);
    }
}

