package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.domain.enums.InterviewType;
import cloudcomputinginha.demo.domain.enums.VoiceType;
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
public class InterviewOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private InterviewFormat interviewFormat;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private InterviewType interviewType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private VoiceType voiceType;

    private Integer questionNumber;

    private Integer answerTime;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    private LocalDateTime endedAt;

    @OneToOne(mappedBy = "interviewOption", cascade = CascadeType.ALL)
    private Interview interview;

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
