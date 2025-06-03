package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.MemberInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberInterviewRepository extends JpaRepository<MemberInterview, Long> {
    List<MemberInterview> interview(Interview interview);

    List<MemberInterview> findByInterviewId(Long interviewId);

    boolean existsByMemberIdAndInterviewId(Long memberId, Long interviewId);

    Optional<MemberInterview> findByMemberIdAndInterviewId(Long memberId, Long interviewId);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.resume JOIN FETCH mi.coverletter JOIN FETCH mi.interview i " +
            "WHERE i.id = :interviewId AND mi.status = 'IN_PROGRESS'")
    List<MemberInterview> findInprogressByInterviewId(Long interviewId);
}