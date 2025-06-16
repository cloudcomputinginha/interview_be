package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;

public interface MemberCommandService {
    MemberInfoResponseDTO updateBasicInfo(Long memberId, MemberInfoRequestDTO request);
}
