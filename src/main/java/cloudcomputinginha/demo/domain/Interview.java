package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.StartType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Interview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String corporateName;

    @Column(nullable = false, length = 20)
    private String jobName;

    private String sessionName; // 그룹 면접의 경우

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private StartType startType;

    private Long hostId;

    @Builder.Default
    private Integer maxParticipants = 1; //일대일 면접을 기준으로 초기화

    @Builder.Default
    private Boolean isOpen = false;

    private LocalDateTime startedAt; // 면접 예정 시작 시간

    private LocalDateTime endedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_option_id", unique = true, nullable = false)
    private InterviewOption interviewOption;

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
