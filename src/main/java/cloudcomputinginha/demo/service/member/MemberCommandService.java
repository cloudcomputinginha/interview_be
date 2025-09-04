package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.request.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.response.MemberInfoResponseDTO;

public interface MemberCommandService {
    MemberInfoResponseDTO registerBasicInfo(Long memberId, MemberInfoRequestDTO.registerInfoDTO request);

    MemberInfoResponseDTO updateBasicInfo(Long memberId, MemberInfoRequestDTO.updateInfoDTO request);

    void deleteMember(Long memberId);
}
