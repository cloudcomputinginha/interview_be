package cloudcomputinginha.demo.service.coverletter;

import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.converter.QnaConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.repository.CoverletterRepository;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.repository.QnaRepository;
import cloudcomputinginha.demo.web.dto.CoverletterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CoverletterCommandServiceImpl implements CoverletterCommandService {
    private final MemberRepository memberRepository;
    private final CoverletterRepository coverletterRepository;
    private final QnaRepository qnaRepository;
    private final MemberInterviewRepository memberInterviewRepository;
    private final CoverletterQueryService coverletterQueryService;

    @Override
    public Coverletter saveCoverletter(Long memberId, CoverletterRequestDTO.createCoverletterDTO requestDTO) {
        Member member = memberRepository.getReferenceById(memberId);
        Coverletter coverletter = CoverletterConverter.toCoverletter(requestDTO, member);
        coverletterRepository.save(coverletter);

        List<Qna> qnaList = requestDTO.getQnaDTOList().stream()
                .map(dto -> QnaConverter.toQna(dto, coverletter))
                .toList();

        qnaRepository.saveAll(qnaList);

        return coverletter;
    }

    @Override
    public void deleteCoverletter(Long coverletterId, Long memberId) {
        Coverletter coverletter = coverletterRepository.findById(coverletterId)
                .orElseThrow(() -> new ResumeHandler(ErrorStatus.COVERLETTER_NOT_FOUND));
        coverletter.validateOwnedBy(memberId);

        // 자소서를 참조하는 MemberInterview 찾아서 연결 해제
        List<MemberInterview> linkedMemberInterview = memberInterviewRepository.findByCoverletterId(coverletterId);
        linkedMemberInterview.forEach(mi -> mi.updateDocument(mi.getResume(), null));

        coverletterRepository.delete(coverletter);
    }
}
