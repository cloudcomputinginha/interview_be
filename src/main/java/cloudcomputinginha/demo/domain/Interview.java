package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.StartType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
    private Integer currentParticipants = 1;

    @Builder.Default
    private Integer maxParticipants = 1; //일대일 면접을 기준으로 초기화

    @Column(length = 255)
    private String noticeUrl; //면접 채용공고 URL

    @Builder.Default
    private Boolean isOpen = false;

    private LocalDateTime startedAt; // 면접 예정 시작 시간

    private LocalDateTime endedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_option_id", unique = true, nullable = false)
    private InterviewOption interviewOption;

    // 면접 삭제 시 참여자 기록도 삭제
    @Builder.Default
    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterview> memberInterviews = new ArrayList<>();

    public void addMemberInterview(MemberInterview memberInterview) {
        memberInterviews.add(memberInterview);
        // memberInterview.setInterview(this);
    }

    public void setInterviewOption(InterviewOption option) {
        this.interviewOption = option;
        option.setInterview(this);
    }

    public void updateStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void updateEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void updateIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public synchronized void increaseCurrentParticipants() {
        if (this.currentParticipants >= this.maxParticipants) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_CAPACITY_EXCEEDED);
        }
        this.currentParticipants += 1;
    }
}
