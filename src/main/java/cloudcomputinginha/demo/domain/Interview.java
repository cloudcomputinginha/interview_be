package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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

    private Long hostId;

    @Builder.Default
    private Integer maxParticipants = 1; //일대일 면접을 기준으로 초기화

    @Column(nullable = false, length = 50)
    private String noticeUrl;

    @Builder.Default
    private Boolean isOpen = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_option_id", unique = true, nullable = false)
    private InterviewOption interviewOption;
}
