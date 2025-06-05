package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberInterviewRepository extends JpaRepository<MemberInterview, Long> {
    List<MemberInterview> interview(Interview interview);

    List<MemberInterview> findByInterviewId(Long interviewId);

    boolean existsByMemberIdAndInterviewId(Long memberId, Long interviewId);

    Optional<MemberInterview> findByMemberIdAndInterviewId(Long memberId, Long interviewId);

    List<MemberInterview> findMemberInterviewByMemberIdAndInterviewIdAndStatus(Long memberId, Long interviewId, InterviewStatus status);

    Integer countMemberInterviewByInterviewId(Long id);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.resume JOIN FETCH mi.coverletter JOIN FETCH mi.interview i " +
            "WHERE i.id = :interviewId AND mi.status = 'IN_PROGRESS'")
    List<MemberInterview> findInprogressByInterviewId(Long interviewId);
}