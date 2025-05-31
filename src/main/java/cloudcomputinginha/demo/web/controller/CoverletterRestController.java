package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.service.CoverletterCommandService;
import cloudcomputinginha.demo.service.CoverletterQueryService;
import cloudcomputinginha.demo.service.QnaQueryService;
import cloudcomputinginha.demo.validation.annotation.ExistCoverletter;
import cloudcomputinginha.demo.validation.annotation.ExistMember;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    @Operation(summary = "자기소개서 생성")
    public ApiResponse<CoverletterResponseDTO.createCoverletterDTO> createCoverletter(@RequestBody @Valid CoverletterRequestDTO.createCoverletterDTO createCoverletterDTO) {
        Coverletter coverletter = coverletterCommandService.saveCoverletter(createCoverletterDTO);
        return ApiResponse.onSuccess(CoverletterConverter.toCreateCoverletterResponseDTO(coverletter));
    }

    @GetMapping("/me")
    @Operation(summary = "나의 자기소개서 리스트 조회")
    public ApiResponse<CoverletterResponseDTO.MyCoverletterListDTO> findMyCoverletter(@RequestParam @ExistMember @NotNull Long memberId) {
        List<Coverletter> coverletterList = coverletterQueryService.findAllCoverletterByMember(memberId);
        return ApiResponse.onSuccess(CoverletterConverter.toMyCoverletterListDTO(coverletterList));
    }

    @GetMapping("/{coverletterId}")
    @Operation(summary = "자기소개서 세부 조회")
    public ApiResponse<CoverletterResponseDTO.CoverletterDetailDTO> getCoverletterDetail(@PathVariable @ExistCoverletter @NotNull Long coverletterId) {
        Coverletter coverletter = coverletterQueryService.getCoverletter(coverletterId);
        List<Qna> qnaList = qnaQueryService.getQnasByCoverletter(coverletterId);

        return ApiResponse.onSuccess(CoverletterConverter.toDetailDTO(coverletter, qnaList));
    }

}

