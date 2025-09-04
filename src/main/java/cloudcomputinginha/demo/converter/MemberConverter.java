package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.web.dto.response.MemberResponseDTO;

public class MemberConverter {
    public static MemberResponseDTO.MemberInfoReponseDTO toMemberInfoResponseDTO(Member member) {
        return MemberResponseDTO.MemberInfoReponseDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .jobType(member.getJobType())
                .introduction(member.getIntroduction())
                .build();
    }
}
