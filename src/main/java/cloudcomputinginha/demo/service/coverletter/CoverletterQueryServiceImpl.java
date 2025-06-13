package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.repository.CoverletterRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoverletterQueryServiceImpl implements CoverletterQueryService {
    private final CoverletterRepository coverletterRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<Coverletter> findAllCoverletterByMember(Long memberId) {
        return coverletterRepository.findAllByMemberId(memberId);
    }

    @Override
    public Coverletter getCoverletter(Long coverletterId) {
        return coverletterRepository.getReferenceById(coverletterId);
    }
}
