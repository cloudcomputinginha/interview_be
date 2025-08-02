package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import cloudcomputinginha.demo.web.dto.CoverletterResponseDTO;

import java.util.List;

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

    public static CoverletterResponseDTO.MyCoverletterListDTO toMyCoverletterListDTO(List<Coverletter> coverletterList) {
        List<CoverletterResponseDTO.MyCoverletterDTO> dtoList = coverletterList.stream()
                .map(c -> CoverletterResponseDTO.MyCoverletterDTO.builder()
                        .coverletterId(c.getId())
                        .corporateName(c.getCorporateName())
                        .jobName(c.getJobName())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();

        return CoverletterResponseDTO.MyCoverletterListDTO.builder()
                .coverletters(dtoList)
                .build();
    }

    public static CoverletterResponseDTO.CoverletterDetailDTO toDetailDTO(Coverletter coverletter) {
        return CoverletterResponseDTO.CoverletterDetailDTO.builder()
                .coverletterId(coverletter.getId())
                .corporateName(coverletter.getCorporateName())
                .jobName(coverletter.getJobName())
                .createdAt(coverletter.getCreatedAt())
                .qnaList(
                        coverletter.getQnas().stream()
                                .map(QnaConverter::toQnaDTO) //batch size 적용
                                .toList()
                )
                .build();
    }
}
