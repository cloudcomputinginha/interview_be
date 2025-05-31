package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;

public class ResumeConverter {
    public static ResumeResponseDTO.CreateResumeResultDTO toCreateResumeResultDTO(Resume resume) {
        return ResumeResponseDTO.CreateResumeResultDTO.builder()
                .resumeId(resume.getId())
                .createdAt(resume.getCreatedAt())
                .build();
    }
}
