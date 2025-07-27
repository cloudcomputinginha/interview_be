package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.embedded.Url;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private NotificationType type;

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "related_url", length = 255, nullable = false))
    private Url url;

    @Column(nullable = false, length = 100)
    private String message;

    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false;

    public void markAsRead() {
        this.isRead = true;
    }

    public void setReceiver(Member member) {
        this.receiver = member;
    }
}
