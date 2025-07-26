package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.apiPayload.code.handler.CoverletterHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coverletter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coverletter_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 100)
    private String corporateName;

    @Column(length = 20)
    private String jobName;

    // 자소서 삭제 시 qna도 함께 삭제되길 원함.
    @Builder.Default
    @OneToMany(mappedBy = "coverletter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qnas = new ArrayList<>();

    public void validateOwnedBy(Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new CoverletterHandler(ErrorStatus.COVERLETTER_NOT_OWNED);
        }
    }

    public void addQna(Qna qna) {
        this.qnas.add(qna);
        qna.setCoverletter(this);
    }
}
