package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;

import java.util.List;

public interface MemberInterviewSocketService {
    void enterWaitingRoom(WaitingRoomActionDTO waitingRoomActionDTO, String sessionId);

    void leaveWaitingRoom(Long interviewId, Long memberId, String sessionId);

    void enterInterview(Long interviewId, List<MemberInterview> memberInterview);
}
