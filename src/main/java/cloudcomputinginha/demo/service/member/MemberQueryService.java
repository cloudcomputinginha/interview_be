package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.response.MemberInfoResponseDTO;

public interface MemberQueryService {
    MemberInfoResponseDTO getBasicInfo(Long memberId);
}
