package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Interview getReferenceWithInterviewOptionById(Long interviewId);
}
