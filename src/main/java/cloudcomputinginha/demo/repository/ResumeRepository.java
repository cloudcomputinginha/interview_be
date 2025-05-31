package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByMemberId(Long memberId);
}
