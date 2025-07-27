package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;

public interface MemberCommandService {
    MemberInfoResponseDTO registerBasicInfo(Long memberId, MemberInfoRequestDTO.registerInfoDTO request);

    MemberInfoResponseDTO updateBasicInfo(Long memberId, MemberInfoRequestDTO.updateInfoDTO request);

    MemberInfoResponseDTO getBasicInfo(Long memberId);

    void deleteMember(Long memberId);
}
