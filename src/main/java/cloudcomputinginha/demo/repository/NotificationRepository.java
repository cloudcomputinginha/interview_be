package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
