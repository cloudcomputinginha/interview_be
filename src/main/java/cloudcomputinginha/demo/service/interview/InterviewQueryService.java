package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewQueryService {
    List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards(Long memberId);

    InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long memberId, Long interviewId);

    /**
     * 현재 시간으로부터 예정된 모든 Interview를 조회한다.
     *
     * @param currentTime
     * @return List<Interview>
     */
    List<Interview> getUpcomingInterviews(LocalDateTime currentTime);

    Optional<Interview> getInterview(Long interviewId);
}
