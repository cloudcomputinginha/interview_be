package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.domain.QInterview;
import cloudcomputinginha.demo.domain.QMemberInterview;
import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InterviewQueryRepositoryImpl implements InterviewQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<InterviewResponseDTO.InterviewGroupCardDTO> findGroupInterviewCards() {
        QInterview interview = QInterview.interview;
        QMemberInterview memberInterview = QMemberInterview.memberInterview;

        return queryFactory
                .select(Projections.constructor(InterviewResponseDTO.InterviewGroupCardDTO.class,
                        interview.id,
                        interview.name,
                        interview.description,
                        interview.sessionName,
                        interview.jobName,
                        interview.interviewOption.interviewType,
                        memberInterview.count().intValue(),
                        interview.maxParticipants,
                        interview.startedAt
                ))
                .from(interview)
                .leftJoin(memberInterview)
                    .on(memberInterview.interview.id.eq(interview.id))
                .where(
                        interview.interviewOption.interviewFormat.eq(InterviewFormat.GROUP),
                        interview.isOpen.isTrue(),
                        interview.startedAt.after(LocalDateTime.now())
                )
                .groupBy(interview.id)
                .orderBy(interview.startedAt.asc())
                .fetch();
    }
}
