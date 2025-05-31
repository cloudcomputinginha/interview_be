package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Coverletter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverletterRepository extends JpaRepository<Coverletter, Long> {
    public List<Coverletter> findAllByMemberId(Long memberId);
}
