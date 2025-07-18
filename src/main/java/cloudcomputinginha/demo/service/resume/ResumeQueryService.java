package cloudcomputinginha.demo.service.resume;

import cloudcomputinginha.demo.domain.Resume;

import java.util.List;

public interface ResumeQueryService {
    public List<Resume> getResumesByMember(Long memberId);

    public Resume getResume(Long resumeId);
}
