package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;

public interface CoverletterCommandService {
    public Coverletter saveCoverletter(CoverletterRequestDTO.createCoverletterDTO coverletterDTO);
}
