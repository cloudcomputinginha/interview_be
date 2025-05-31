package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.Coverletter;

import java.util.List;

public interface CoverletterQueryService {
    List<Coverletter> findAllCoverletterByMember(Long memberId);

    Coverletter getCoverletter(Long coverletterId);
}
