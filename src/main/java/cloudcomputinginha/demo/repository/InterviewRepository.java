package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    @Query("SELECT i, io FROM Interview i " +
            "JOIN FETCH i.interviewOption io " +
            "WHERE i.id = :interviewId")
    Interview findWithInterviewOptionById(Long interviewId);

    List<Interview> findAllByStartedAtAfterAndEndedAtIsNull(LocalDateTime startedAtAfter);

    @Query("SELECT i, io FROM Interview i " +
            "JOIN FETCH i.interviewOption io " +
            "JOIN i.memberInterviews mi " +
            "WHERE mi.resume.id = :resumeId")
    List<Interview> findByResumeId(Long resumeId);

    @Query("SELECT i FROM Interview i " +
            "JOIN FETCH i.interviewOption io " +
            "JOIN i.memberInterviews mi " +
            "WHERE mi.coverletter.id = :coverletterId")
    List<Interview> findByCoverletterId(Long coverletterId);

}
