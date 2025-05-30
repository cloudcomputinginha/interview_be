package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.MemberInterview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberInterviewRepository extends JpaRepository<MemberInterview, Long> {
    List<MemberInterview> interview(Interview interview);

    Optional<MemberInterview> findByMemberIdAndInterviewId(Long memberId, Long interviewId);

    boolean existsByMemberIdAndInterviewId(Long memberId, Long interviewId);

    Integer countMemberInterviewByInterviewId(Long id);
}