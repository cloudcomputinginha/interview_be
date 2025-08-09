package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

public interface InterviewCommandService {

    /**
     * 면접을 생성하는 메서드
     * 만약 면접 시작 종류가 SCHEDULED라면, 면접 시작 스케줄링을 위해 InterviewScheduler에 등록된다.
     *
     * @param request 면접 생성 요청 DTO
     * @param memberId 면접을 생성하는 사용자 id
     * @return 면접 생성 결과 DTO
     */
    InterviewResponseDTO.InterviewCreateResultDTO createInterview(InterviewRequestDTO.InterviewCreateDTO request, Long memberId);

    /**
     * 그룹 면접을 시작하는 메서드
     *
     * @param memberId 면접을 시작하는 사용자 id
     * @param interviewId 시작할 면접 id
     * @param isAutomaticStart 면접 자동 시작 여부
     * @return 면접 시작 응답 DTO
     */
    public InterviewResponseDTO.InterviewStartResponseDTO startInterview(Long memberId, Long interviewId, Boolean isAutomaticStart);

    /**
     * 면접 정보를 업데이트하는 메서드
     *
     * @param memberId 면접 정보를 업데이트하려는 사용자 id(host id만 수정 가능)
     * @param interviewId 수정할 면접 id
     * @param request 수정할 내용 DTO
     * @return 면접 수정 응답 DTO
     */
    InterviewResponseDTO.InterviewUpdateResponseDTO updateInterview(Long memberId, Long interviewId, InterviewRequestDTO.InterviewUpdateDTO request);

    /**
     * 면접 종료 메서드(면접 종료 시간 업데이트)
     *
     * @param memberId 면접을 종료하려는 사용자 id
     * @param interviewId 종료할 면접 id
     * @param endInterviewRequestDTO 면접 종료 시간 DTO
     * @return 면접 엔티티
     */
    Interview terminateInterview(Long memberId, Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO);
}
