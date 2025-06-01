package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.converter.CoverletterConverter;
import cloudcomputinginha.demo.converter.QnaConverter;
import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.repository.CoverletterRepository;
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

    @Override
    public Coverletter saveCoverletter(CoverletterRequestDTO.createCoverletterDTO requestDTO) {
        Member member = memberRepository.getReferenceById(requestDTO.getMemberId());
        Coverletter coverletter = CoverletterConverter.toCoverletter(requestDTO, member);
        coverletterRepository.save(coverletter);

        List<Qna> qnaList = requestDTO.getQnaDTOList().stream()
                .map(dto -> QnaConverter.toQna(dto, coverletter))
                .toList();

        qnaRepository.saveAll(qnaList);

        return coverletter;
    }
}
