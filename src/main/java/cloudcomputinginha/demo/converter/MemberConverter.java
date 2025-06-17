package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;

public class MemberConverter {
    public static MemberInfoResponseDTO toMemberInfoResponseDTO(Member member) {
        return MemberInfoResponseDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .jobType(member.getJobType())
                .introduction(member.getIntroduction())
                .build();
    }
}
