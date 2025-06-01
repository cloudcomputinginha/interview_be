package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaQueryServiceImpl implements QnaQueryService {
    private final QnaRepository qnaRepository;

    @Override
    public List<Qna> getQnasByCoverletter(Long coverletterId) {
        return qnaRepository.findAllByCoverletterId(coverletterId);
    }
}
