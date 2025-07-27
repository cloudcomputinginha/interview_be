package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.repository.CoverletterRepository;
import cloudcomputinginha.demo.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoverletterQueryServiceImpl implements CoverletterQueryService {
    private final CoverletterRepository coverletterRepository;
    private final InterviewRepository interviewRepository;

    @Override
    public List<Coverletter> findAllCoverletterByMember(Long memberId) {
        return coverletterRepository.findAllByMemberId(memberId);
    }

    @Override
    public Coverletter getCoverletter(Long coverletterId) {
        return coverletterRepository.getReferenceById(coverletterId);
    }

    @Override
    public List<Interview> getInterviewsByCoverletter(Long coverletterId) {
        return interviewRepository.findByCoverletterId(coverletterId);
    }
}
