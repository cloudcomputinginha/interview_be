package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;

import java.util.List;

public interface MemberInterviewSocketService {
    /**
     * 면접 대기실에 입장하는 메서드
     * 입장하려는 사용자의 면접 상태, 문서 상태를 검증하고,
     * 해당 사용자의 면접 상태를 IN_PROGRESS로 변경하고,
     * 대기실에 입장한 사용자에게 알림을 발송한다.
     *
     * @param waitingRoomActionDTO 대기실 액션 DTO
     * @param sessionId 세션 ID
     */
    void enterWaitingRoom(WaitingRoomActionDTO waitingRoomActionDTO, String sessionId);

    /**
     * 면접 대기실에서 퇴장하는 메서드
     * 퇴장하려는 사용자의 면접 상태를 검증하고,
     * 해당 사용자의 면접 상태를 SCHEDULED로 변경하고,
     * 대기실에서 퇴장한 사용자에게 알림을 발송한다.
     *
     * @param interviewId 면접 ID
     * @param memberId 면접 참여자 ID
     * @param sessionId 세션 ID
     */
    void leaveWaitingRoom(Long interviewId, Long memberId, String sessionId);

    /**
     * 면접에 참여하는 메서드
     * 면접 참여 시, 해당 사용자 면접의 상태를 IN_PROGRESS에서 DONE으로 변경하고,
     * 면접 불참 시, 해당 사용자 면접 상태를 SCHEDULED에서 NO_SHOW로 변경한다.
     * 또한 참가중인 면접자가 있을 경우, 알림을 발송한다.
     *
     * @param interviewId 면접 ID
     * @param memberInterview 면접에 연관된 모든 멤버 인터뷰 리스트
     */
    void enterInterview(Long interviewId, List<MemberInterview> memberInterview);
}
