package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.web.dto.request.CoverletterRequestDTO;

public interface CoverletterCommandService {
    public Coverletter saveCoverletter(Long memberId, CoverletterRequestDTO.createCoverletterDTO coverletterDTO);

    void deleteCoverletter(Long coverletterId, Long memberId);
}
