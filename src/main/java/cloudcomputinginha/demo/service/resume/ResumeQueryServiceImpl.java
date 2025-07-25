package cloudcomputinginha.demo.service.resume;

import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeQueryServiceImpl implements ResumeQueryService {
    private final ResumeRepository resumeRepository;

    @Override
    public List<Resume> getResumesByMember(Long memberId) {
        return resumeRepository.findAllByMemberId(memberId);
    }

    @Override
    public Resume getResume(Long resumeId) {
        return resumeRepository.getReferenceById(resumeId);
    }
}
