package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    void deleteByIsGuestTrueAndCreatedAtBefore(LocalDateTime threshold);
}
