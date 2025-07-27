package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;

public interface CoverletterCommandService {
    public Coverletter saveCoverletter(Long memberId, CoverletterRequestDTO.createCoverletterDTO coverletterDTO);

    void deleteCoverletter(Long coverletterId, Long memberId);
}
