package cloudcomputinginha.demo.service.member;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.MemberConverter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.web.dto.MemberInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberInfoResponseDTO getBasicInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberConverter.toMemberInfoResponseDTO(member);
    }
}
