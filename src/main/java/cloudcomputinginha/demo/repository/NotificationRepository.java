package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndCreatedAtAfterOrderByCreatedAtDesc(Long memberId, LocalDateTime oneMonthAgo);

    Optional<Notification> findByIdAndReceiverId(Long notificationId, Long memberId);
}
