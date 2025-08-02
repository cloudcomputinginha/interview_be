package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;

public interface MemberQueryService {
    MemberInfoResponseDTO getBasicInfo(Long memberId);
}
