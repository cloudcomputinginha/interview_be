package cloudcomputinginha.demo.service.resume;

import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.web.dto.ResumeRequestDTO;

public interface ResumeCommandService {
    public Resume saveResume(Long memberId, ResumeRequestDTO.ResumeCreateDTO resumeCreateDTO);
}
