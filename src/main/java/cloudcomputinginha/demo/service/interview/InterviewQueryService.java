package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.response.InterviewResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewQueryService {
    List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards(Long memberId);

    InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long memberId, Long interviewId);

    /**
     * 현재 시간으로부터 예정된 모든 Interview를 조회한다.
     *
     * @param currentTime 현재 시간
     * @return List<Interview>
     */
    List<Interview> getUpcomingInterviews(LocalDateTime currentTime);

    Optional<Interview> getInterview(Long interviewId);

    /**
     * 면접 시작 정보를 조회하는 메서드
     *
     * @param memberId 조회하는 사용자 id
     * @param interviewId 조회할 면접 id
     * @return InterviewResponseDTO.InterviewStartResponseDTO
     */
    InterviewResponseDTO.InterviewStartResponseDTO getStartInterviewInfo(Long memberId, Long interviewId);
}
