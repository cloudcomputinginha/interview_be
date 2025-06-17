package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> findRecentNotifications(Long memberId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return notificationRepository.findByReceiverIdAndCreatedAtAfterOrderByCreatedAtDesc(
                memberId, oneMonthAgo
        );
    }
}
