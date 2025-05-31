package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.Qna;

import java.util.List;

public interface QnaQueryService {
    List<Qna> getQnasByCoverletter(Long coverletterId);
}
