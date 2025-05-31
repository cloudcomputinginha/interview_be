package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
