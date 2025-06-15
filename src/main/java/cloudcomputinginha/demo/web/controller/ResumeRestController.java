package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.ResumeConverter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.service.resume.ResumeCommandService;
import cloudcomputinginha.demo.service.resume.ResumeQueryService;
import cloudcomputinginha.demo.service.resume.ResumeS3Service;
import cloudcomputinginha.demo.validation.annotation.ExistMember;
import cloudcomputinginha.demo.validation.annotation.ExistResume;
import cloudcomputinginha.demo.validation.annotation.ValidFileName;
import cloudcomputinginha.demo.web.dto.ResumeRequestDTO;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "이력서 API")
@RequestMapping("/resumes/")
@RequiredArgsConstructor
public class ResumeRestController {
    private final ResumeS3Service s3PresignedService;
    private final ResumeCommandService resumeCommandService;
    private final ResumeQueryService resumeQueryService;
    private final MemberRepository memberRepository;

    @GetMapping("/upload")
    @Operation(summary = "이력서를 업로드할 presignedURL 발급", description = "업로드할 파일을 이름을 넘길 떄, 확장자를 포함합니다.")
    public ApiResponse<ResumeResponseDTO.PresignedUploadDTO> getPresignedUploadUrl(@RequestParam @ValidFileName String fileName) {
        ResumeResponseDTO.PresignedUploadDTO uploadUrlDTO = s3PresignedService.getUploadPresignedURL(fileName);
        return ApiResponse.onSuccess(uploadUrlDTO);
    }

    @PostMapping("/upload")
    @Operation(summary = "S3에 저장된 이력서 메타데이터 저장")
    public ApiResponse<ResumeResponseDTO.CreateResumeResultDTO> saveResume(@AuthenticationPrincipal Long memberId, @RequestBody @Valid ResumeRequestDTO.ResumeCreateDTO resumeCreateDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Resume resume = resumeCommandService.saveResume(memberId, resumeCreateDTO);
        return ApiResponse.onSuccess(ResumeConverter.toCreateResumeResultDTO(resume));
    }

    @GetMapping
    @Operation(summary = "사용자의 이력서 리스트 조회")
    public ApiResponse<ResumeResponseDTO.ResumeListDTO> getResumeList(
            @AuthenticationPrincipal Long memberId
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Resume> resumes = resumeQueryService.getResumesByMember(memberId);
        return ApiResponse.onSuccess(ResumeConverter.toResumeListDTO(resumes));
    }

    @GetMapping("/{resumeId}")
    @Operation(summary = "이력서 상세 조회")
    public ApiResponse<ResumeResponseDTO.ResumeDetailDTO> getResumeDetail(
            @PathVariable @NotNull @ExistResume Long resumeId,
            @AuthenticationPrincipal Long memberId
    ) {
        Resume resume = resumeQueryService.getResume(resumeId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!resume.getMember().getId().equals(memberId)) {
            throw new ResumeHandler(ErrorStatus.RESUME_NOT_OWNED);
        }

        return ApiResponse.onSuccess(ResumeConverter.toResumeDetailDTO(resume));
    }
}
