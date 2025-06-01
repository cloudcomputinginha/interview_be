package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;

public interface CoverletterCommandService {
    public Coverletter saveCoverletter(CoverletterRequestDTO.createCoverletterDTO coverletterDTO);
}
