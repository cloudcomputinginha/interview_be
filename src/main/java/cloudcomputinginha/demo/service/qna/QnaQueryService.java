package cloudcomputinginha.demo.service.qna;

import cloudcomputinginha.demo.domain.Qna;

import java.util.List;

public interface QnaQueryService {
    List<Qna> getQnasByCoverletter(Long coverletterId);
}
