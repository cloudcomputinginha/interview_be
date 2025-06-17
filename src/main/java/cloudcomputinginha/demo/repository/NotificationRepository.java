package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndCreatedAtAfterOrderByCreatedAtDesc(Long memberId, LocalDateTime oneMonthAgo);
}
