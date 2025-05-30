package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
