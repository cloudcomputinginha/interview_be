package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    @Query("SELECT q FROM Qna q WHERE q.coverletter.id = :coverletterId")
    List<Qna> findAllByCoverletterId(Long coverletterId);

    @Query("SELECT q FROM Qna q WHERE q.coverletter.id IN :coverletterIds")
    List<Qna> findAllByCoverletterIds(List<Long> coverletterIds);
}
