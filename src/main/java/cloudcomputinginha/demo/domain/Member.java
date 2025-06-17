package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(length = 10)
    private SocialProvider socialProvider;

    @Column(length = 100)
    private String providerId;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Coverletter> coverLetters = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberInterview> memberInterviews = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Interview> hostedInterviews = new ArrayList<>();


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

    // 게스트 로그인
    private boolean isGuest;

    public static Member createGuest(String randomName) {
        Member member = new Member();
        member.email = "guest_" + UUID.randomUUID().toString().substring(0, 5)+ "@guest.com";
        member.name = randomName;
        member.isGuest = true;
        return member;
    }
}
