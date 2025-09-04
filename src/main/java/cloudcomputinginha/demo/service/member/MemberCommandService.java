package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.web.dto.request.MemberInfoRequestDTO;
import cloudcomputinginha.demo.web.dto.response.MemberResponseDTO;

public interface MemberCommandService {
    MemberResponseDTO.MemberInfoReponseDTO registerBasicInfo(Long memberId, MemberInfoRequestDTO.registerInfoDTO request);

    MemberResponseDTO.MemberInfoReponseDTO updateBasicInfo(Long memberId, MemberInfoRequestDTO.updateInfoDTO request);

    void deleteMember(Long memberId);
}
