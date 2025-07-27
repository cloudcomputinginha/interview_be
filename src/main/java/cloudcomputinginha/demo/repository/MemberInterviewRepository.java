package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.MemberInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberInterviewRepository extends JpaRepository<MemberInterview, Long> {
    boolean existsByMemberIdAndInterviewId(Long memberId, Long interviewId);

    List<MemberInterview> findByResumeId(Long resumeId);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.resume JOIN FETCH mi.coverletter JOIN FETCH mi.interview i " +
            "WHERE i.id = :interviewId")
    List<MemberInterview> findByInterviewId(Long interviewId);

    List<MemberInterview> findWithMemberByInterviewId(Long interviewId);

    Optional<MemberInterview> findByMemberIdAndInterviewId(Long memberId, Long interviewId);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.resume JOIN FETCH mi.coverletter JOIN FETCH mi.interview i " +
            "WHERE i.id = :interviewId AND mi.status = 'IN_PROGRESS'")
    List<MemberInterview> findByInterviewIdAndInprogress(Long interviewId);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.interview i JOIN FETCH i.interviewOption o " +
            "WHERE mi.member.id = :memberId ORDER BY i.startedAt DESC ")
    List<MemberInterview> findAllForMyPage(Long memberId);

    @Query("SELECT mi FROM MemberInterview mi " +
            "JOIN FETCH mi.member JOIN FETCH mi.interview i " +
            "WHERE mi.id = :memberInterviewId AND i.id =:interviewId")
    Optional<MemberInterview> findWithMemberAndInterviewByIdAndInterviewId(Long memberInterviewId, Long interviewId);
}