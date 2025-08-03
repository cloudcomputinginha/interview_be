package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.converter.ResumeConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.infra.ai.AiOpenFeignService;
import cloudcomputinginha.demo.service.resume.ResumeCommandService;
import cloudcomputinginha.demo.service.resume.ResumeQueryService;
import cloudcomputinginha.demo.service.resume.ResumeS3Service;
import cloudcomputinginha.demo.validation.annotation.ValidFileName;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import cloudcomputinginha.demo.web.dto.ResumeRequestDTO;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "이력서 API")
@RequestMapping("/resumes")
@RequiredArgsConstructor
@Validated
public class ResumeRestController {
    private final ResumeS3Service s3PresignedService;
    private final ResumeCommandService resumeCommandService;
    private final ResumeQueryService resumeQueryService;
    private final AiOpenFeignService aiOpenFeignService;

    @GetMapping("/upload")
    @Operation(summary = "이력서를 업로드할 presignedURL 발급", description = "업로드할 파일을 이름을 넘길 떄, 확장자를 포함합니다.")
    public ApiResponse<ResumeResponseDTO.PresignedUploadDTO> getPresignedUploadUrl(@RequestParam @ValidFileName String fileName) {
        ResumeResponseDTO.PresignedUploadDTO uploadUrlDTO = s3PresignedService.getUploadPresignedURL(fileName);
        return ApiResponse.onSuccess(uploadUrlDTO);
    }

    @PostMapping("/upload")
    @Operation(summary = "S3에 저장된 이력서 메타데이터 저장")
    public ApiResponse<ResumeResponseDTO.CreateResumeResultDTO> saveResume(@AuthenticationPrincipal Long memberId, @RequestBody @Valid ResumeRequestDTO.ResumeCreateDTO resumeCreateDTO) {
        String interviewOcrResult = aiOpenFeignService.getInterviewOcrResult(resumeCreateDTO.getFileUrl());

        Resume resume = resumeCommandService.saveResume(memberId, resumeCreateDTO, interviewOcrResult);

        return ApiResponse.onSuccess(ResumeConverter.toCreateResumeResultDTO(resume));
    }

    @GetMapping
    @Operation(summary = "사용자의 이력서 리스트 조회")
    public ApiResponse<ResumeResponseDTO.ResumeListDTO> getResumeList(
            @AuthenticationPrincipal Long memberId
    ) {
        List<Resume> resumes = resumeQueryService.getResumesByMember(memberId);
        return ApiResponse.onSuccess(ResumeConverter.toResumeListDTO(resumes));
    }

    @GetMapping("/{resumeId}")
    @Operation(summary = "이력서 상세 조회")
    public ApiResponse<ResumeResponseDTO.ResumeDetailDTO> getResumeDetail(
            @PathVariable Long resumeId,
            @AuthenticationPrincipal Long memberId
    ) {
        Resume resume = resumeQueryService.getResume(resumeId);
        resume.validateOwnedBy(memberId);

        return ApiResponse.onSuccess(ResumeConverter.toResumeDetailDTO(resume));
    }

    @GetMapping("/{resumeId}/interviews")
    @Operation(summary = "해당 이력서를 사용중인 인터뷰 리스트 조회")
    public ApiResponse<List<InterviewResponseDTO.InterviewGroupCardDTO>> getResumeInterviews(
            @PathVariable Long resumeId,
            @AuthenticationPrincipal Long memberId
    ) {
        Resume resume = resumeQueryService.getResume(resumeId);
        resume.validateOwnedBy(memberId);

        List<Interview> interviews = resumeQueryService.getInterviewsByResume(resumeId);
        List<InterviewResponseDTO.InterviewGroupCardDTO> interviewCards = interviews.stream()
                .map(InterviewConverter::toInterviewGroupCardDTO)
                .toList();
        return ApiResponse.onSuccess(interviewCards);
    }

    @DeleteMapping("/{resumeId}")
    @Operation(summary = "이력서 삭제")
    public ApiResponse<Void> deleteResume(
            @PathVariable Long resumeId,
            @AuthenticationPrincipal Long memberId
    ) {
        resumeCommandService.deleteResume(resumeId, memberId);
        return ApiResponse.onSuccess(null);
    }
}
