package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Interview getReferenceWithInterviewOptionById(Long interviewId);

    List<Interview> findAllByStartedAtAfterAndEndedAtIsNull(LocalDateTime startedAtAfter);
}
