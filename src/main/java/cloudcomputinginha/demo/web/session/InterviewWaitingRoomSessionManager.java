package cloudcomputinginha.demo.web.session;

import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.SessionInfoDTO;

import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InterviewWaitingRoomSessionManager {

    private final InMemorySessionManager<SessionInfoDTO> sessionManager;

    public InterviewWaitingRoomSessionManager() {
        this.sessionManager = new InMemorySessionManager<>();
    }

    public void put(String sessionId, SessionInfoDTO info) {
        sessionManager.put(sessionId, info);
    }

    public SessionInfoDTO get(String sessionId) {
        return sessionManager.get(sessionId);
    }

    public SessionInfoDTO remove(String sessionId) {
        return sessionManager.remove(sessionId);
    }

    public List<Long> getParticipantMemberIds(Long interviewId) {
        return sessionManager.getByPredicate(info -> info.interviewId().equals(interviewId)).stream()
            .map(SessionInfoDTO::memberId)
            .distinct()
            .collect(Collectors.toList());
    }

    public void clear() {
        sessionManager.clear();
    }
}