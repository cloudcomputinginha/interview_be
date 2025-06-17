package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.domain.Notification;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> findRecentNotifications(Long memberId);
}
