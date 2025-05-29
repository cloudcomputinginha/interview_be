package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.domain.common.BaseEntity;
import cloudcomputinginha.demo.domain.enums.FileType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 100, nullable = false)
    private String fileName;

    @Column(length = 255, nullable = false, unique = true)
    private String fileUrl;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(length = 4, nullable = false)
    private FileType fileType;
}
