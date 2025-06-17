package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndCreatedAtAfterOrderByCreatedAtDesc(Long memberId, LocalDateTime oneMonthAgo);

    Optional<Notification> findByIdAndReceiverId(Long notificationId, Long memberId);

    @Query("SELECT COUNT(n) FROM Notification n " +
            "WHERE n.receiver.id = :memberId " +
            "AND n.createdAt >= :localDateTime " +
            "AND n.isRead = false")
    Long countUnread(Long memberId, LocalDateTime localDateTime);
}
