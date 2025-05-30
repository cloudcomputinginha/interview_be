package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;

public class MemberInterviewConverter {
    public static MemberInterviewResponseDTO.MemberInterviewStatusDTO toMemberInterviewStatusDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.MemberInterviewStatusDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .status(memberInterview.getStatus())
                .updatedAt(memberInterview.getUpdatedAt())
                .build();
    }
}
