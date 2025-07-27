package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Interview;

import java.util.List;

public interface CoverletterQueryService {
    List<Coverletter> findAllCoverletterByMember(Long memberId);

    List<Interview> getInterviewsByCoverletter(Long coverletterId);

    Coverletter getOwnedCoverletter(Long coverletterId, Long memberId);
}
