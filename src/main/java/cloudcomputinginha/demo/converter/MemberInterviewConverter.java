package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;

public class MemberInterviewConverter {
    public static MemberInterviewResponseDTO.MemberInterviewStatusDTO toMemberInterviewStatusDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.MemberInterviewStatusDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .status(memberInterview.getStatus())
                .updatedAt(memberInterview.getUpdatedAt())
                .build();
    }

    public static MemberInterview toMemberInterview(Member member, Interview interview, Resume resume, Coverletter coverletter) {
        return MemberInterview.builder()
                .member(member)
                .interview(interview)
                .resume(resume)
                .coverletter(coverletter)
                .build();
    }

    public static MemberInterviewResponseDTO.CreateMemberInterviewDTO toMemberInterviewResultDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.CreateMemberInterviewDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .createdAt(memberInterview.getCreatedAt())
                .build();
    }
}
