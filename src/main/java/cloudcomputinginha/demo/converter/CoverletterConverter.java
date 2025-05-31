package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;

public class CoverletterConverter {
    public static Coverletter toCoverletter(CoverletterRequestDTO.createCoverletterDTO createCoverletterDTO, Member member) {
        return Coverletter.builder()
                .member(member)
                .corporateName(createCoverletterDTO.getCorporateName())
                .jobName(createCoverletterDTO.getJobName())
                .build();
    }

    public static CoverletterResponseDTO.createCoverletterDTO toCreateCoverletterResponseDTO(Coverletter coverletter) {
        return CoverletterResponseDTO.createCoverletterDTO.builder()
                .coverletterId(coverletter.getId())
                .createdAt(coverletter.getCreatedAt())
                .build();
    }
}
