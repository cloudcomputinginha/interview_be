package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
