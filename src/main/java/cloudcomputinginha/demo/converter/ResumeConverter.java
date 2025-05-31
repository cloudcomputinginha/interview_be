package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;

import java.util.List;

public class ResumeConverter {
    public static ResumeResponseDTO.CreateResumeResultDTO toCreateResumeResultDTO(Resume resume) {
        return ResumeResponseDTO.CreateResumeResultDTO.builder()
                .resumeId(resume.getId())
                .createdAt(resume.getCreatedAt())
                .build();
    }

    public static ResumeResponseDTO.ResumeDTO toResumeDTO(Resume resume) {
        return ResumeResponseDTO.ResumeDTO.builder()
                .resumeId(resume.getId())
                .fileName(resume.getFileName())
                .fileSize(resume.getFileSize())
                .build();
    }

    public static ResumeResponseDTO.ResumeDetailDTO toResumeDetailDTO(Resume resume) {
        return ResumeResponseDTO.ResumeDetailDTO.builder()
                .resumeId(resume.getId())
                .fileName(resume.getFileName())
                .fileUrl(resume.getFileUrl())
                .fileSize(resume.getFileSize())
                .fileType(resume.getFileType())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }

    public static ResumeResponseDTO.ResumeListDTO toResumeListDTO(List<Resume> resumes) {
        List<ResumeResponseDTO.ResumeDTO> resumeList = resumes.stream()
                .map(ResumeConverter::toResumeDTO)
                .toList();
        return ResumeResponseDTO.ResumeListDTO.builder()
                .resumes(resumeList)
                .build();
    }
}
