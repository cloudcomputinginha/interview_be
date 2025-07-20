package cloudcomputinginha.demo.service.resume;

import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.domain.enums.FileType;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.repository.ResumeRepository;
import cloudcomputinginha.demo.web.dto.ResumeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeCommandServiceImpl implements ResumeCommandService {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;

    @Override
    public Resume saveResume(Long memberId, ResumeRequestDTO.ResumeCreateDTO resumeCreateDTO) {
        if (!resumeCreateDTO.getFileName().toLowerCase().endsWith(".pdf") || !resumeCreateDTO.getFileUrl().toLowerCase().endsWith(".pdf")) {
            throw new ResumeHandler(ErrorStatus.RESUME_FILE_TYPE_INVALID);
        }
        Member member = memberRepository.getReferenceById(memberId);
        Resume resume = Resume.builder()
                .member(member)
                .fileName(resumeCreateDTO.getFileName())
                .fileUrl(resumeCreateDTO.getFileUrl())
                .fileSize(resumeCreateDTO.getFileSize())
                .fileType(FileType.PDF)
                .build();
        return resumeRepository.save(resume);
    }
}
