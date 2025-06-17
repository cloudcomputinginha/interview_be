package cloudcomputinginha.demo.config.auth;

import cloudcomputinginha.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GuestAccountCleaner {
    private final MemberRepository memberRepository;

    @Scheduled(fixedDelay = 3600000)
    @Transactional
    public void deleteOldGuestAccounts() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        memberRepository.deleteByIsGuestTrueAndCreatedAtBefore(threshold);
    }
}
