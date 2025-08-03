package cloudcomputinginha.demo.service.resume;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.domain.enums.FileType;
import cloudcomputinginha.demo.infra.ai.AiOpenFeignService;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.repository.ResumeRepository;
import cloudcomputinginha.demo.web.dto.ResumeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeCommandServiceImpl implements ResumeCommandService {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    private final MemberInterviewRepository memberInterviewRepository;
    private final AiOpenFeignService aiOpenFeignService;

    @Override
    public Resume saveResume(Long memberId, ResumeRequestDTO.ResumeCreateDTO resumeCreateDTO) {
        if (!resumeCreateDTO.getFileName().toLowerCase().endsWith(".pdf")) {
            throw new ResumeHandler(ErrorStatus.RESUME_FILE_TYPE_INVALID);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String interviewOcrResult = aiOpenFeignService.getInterviewOcrResult(resumeCreateDTO.getFileUrl());

        Resume resume = Resume.builder()
                .member(member)
                .fileName(resumeCreateDTO.getFileName())
                .fileUrl(resumeCreateDTO.getFileUrl())
                .fileSize(resumeCreateDTO.getFileSize())
                .fileType(FileType.PDF)
                .ocrResult(interviewOcrResult)
                .build();

        member.addResume(resume);

        return resumeRepository.save(resume);
    }

    @Transactional
    public void deleteResume(Long memberId, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeHandler(ErrorStatus.RESUME_NOT_FOUND));
        resume.validateOwnedBy(memberId);

        // 이력서를 참조하는 MemberInterview 찾아서 연결 해제
        List<MemberInterview> linkedMemberInterview = memberInterviewRepository.findByResumeId(resumeId);
        linkedMemberInterview.forEach(mi -> mi.updateDocument(null, mi.getCoverletter()));

        resumeRepository.delete(resume);
    }

}
