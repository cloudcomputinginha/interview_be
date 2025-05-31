package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.web.dto.QnaRequestDTO;

public class QnaConverter {
    public static Qna toQna(QnaRequestDTO.createQnaDTO createQnaDTO, Coverletter coverletter) {
        return Qna.builder()
                .coverletter(coverletter)
                .question(createQnaDTO.getQuestion())
                .answer(createQnaDTO.getAnswer())
                .build();
    }
}
