package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.response.MemberResponseDTO;

public interface MemberQueryService {
    MemberResponseDTO.MemberInfoReponseDTO getBasicInfo(Long memberId);
}
