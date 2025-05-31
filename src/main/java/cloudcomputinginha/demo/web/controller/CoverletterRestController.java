package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.service.CoverletterCommandService;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자기소개서 API")
@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
@Validated
public class CoverletterRestController {
    private final CoverletterCommandService coverletterCommandService;

    @PostMapping
    public ApiResponse<CoverletterResponseDTO.createCoverletterDTO> createCoverletter(@RequestBody @Valid CoverletterRequestDTO.createCoverletterDTO createCoverletterDTO) {
        Coverletter coverletter = coverletterCommandService.saveCoverletter(createCoverletterDTO);
        return ApiResponse.onSuccess(CoverletterConverter.toCreateCoverletterResponseDTO(coverletter));
    }
}

