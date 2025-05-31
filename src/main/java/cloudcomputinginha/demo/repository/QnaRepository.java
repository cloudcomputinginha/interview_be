package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findAllByCoverletterId(Long coverletterId);
}
