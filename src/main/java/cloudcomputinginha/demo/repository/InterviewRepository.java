package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Interview getReferenceWithInterviewOptionById(Long interviewId);

    List<Interview> findAllByStartedAtAfter(LocalDateTime startedAtAfter);
}
