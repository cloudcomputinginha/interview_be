package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.Qna;
import cloudcomputinginha.demo.web.dto.QnaRequestDTO;
import cloudcomputinginha.demo.web.dto.QnaResponseDTO;

public class QnaConverter {
    public static Qna toQna(QnaRequestDTO.createQnaDTO createQnaDTO, Coverletter coverletter) {
        return Qna.builder()
                .coverletter(coverletter)
                .question(createQnaDTO.getQuestion())
                .answer(createQnaDTO.getAnswer())
                .build();
    }

    public static QnaResponseDTO.QnaDTO toQnaDTO(Qna qna) {
        return QnaResponseDTO.QnaDTO.builder()
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .build();
    }
}
