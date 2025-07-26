package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 10)
    private String jobType;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private SocialProvider socialProvider;

    @Column(length = 100, nullable = false)
    private String providerId;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    // 회원 탈퇴 시 자소서도 함께 삭제되길 원함.
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coverletter> coverletters = new ArrayList<>();

    // 회원 탈퇴 시 이력서도 함께 삭제되길 원함.
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    // 회원 탈퇴 시 회원이 참여한 면접 기록도 함께 삭제되길 원함.
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterview> memberInterviews = new ArrayList<>();

    // 회원 탈퇴 시 해당 회원에게 발송된 알림도 함께 삭제되길 원함.
    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void registerInfo(String phone, String jobType, String introduction) {
        this.phone = phone;
        this.jobType = jobType;
        this.introduction = introduction;
    }

    public void updateInfo(String name, String phone, String jobType, String introduction) {
        this.name = name;
        this.phone = phone;
        this.jobType = jobType;
        this.introduction = introduction;
    }
}
