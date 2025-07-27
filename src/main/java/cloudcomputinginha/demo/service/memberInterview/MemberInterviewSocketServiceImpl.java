package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.MemberInterviewSocketMessageConverter;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.infra.messaging.WaitingRoomEventPublisher;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;
import cloudcomputinginha.demo.web.session.InterviewWaitingRoomSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberInterviewSocketServiceImpl implements MemberInterviewSocketService {

    private final MemberInterviewRepository memberInterviewRepository;
    private final WaitingRoomEventPublisher eventPublisher;
    private final InterviewWaitingRoomSessionManager sessionManager;

    @Override
    public void enterWaitingRoom(WaitingRoomActionDTO actionDTO, String sessionId) {

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(
                        actionDTO.getMemberId(), actionDTO.getInterviewId())
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        if (hasInterviewEnded(memberInterview)) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_ALREADY_STARTED);
        }

        memberInterview.updateStatus(InterviewStatus.IN_PROGRESS);
        sessionManager.put(sessionId,
                MemberInterviewSocketMessageConverter.toSessionInfoDTO(actionDTO));

        eventPublisher.notifyEnter(actionDTO);
        eventPublisher.broadcastParticipants(actionDTO.getInterviewId());
    }

    @Override
    public void leaveWaitingRoom(Long interviewId, Long memberId, String sessionId) {

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        if (hasInterviewEnded(memberInterview)) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_ALREADY_STARTED);
        }

        sessionManager.remove(sessionId);

        memberInterview.updateStatus(InterviewStatus.SCHEDULED);

        eventPublisher.broadcastParticipants(interviewId);
    }

    @Override
    public void enterInterview(Long interviewId, List<MemberInterview> memberInterviews) {
        AtomicInteger cnt = new AtomicInteger();

        memberInterviews.forEach((memberInterview) -> {
            if (memberInterview.getStatus().equals(InterviewStatus.IN_PROGRESS)) {
                memberInterview.updateStatus(InterviewStatus.DONE);
                cnt.getAndIncrement();
            } else if (memberInterview.getStatus().equals(InterviewStatus.SCHEDULED)) {
                memberInterview.updateStatus(InterviewStatus.NO_SHOW);
            }
        });

        // 참가중인 면접자가 있을 시 알림 발송
        if (cnt.get() > 0) {
            eventPublisher.broadcastInterviewStart(interviewId);
        }

    }

    private boolean hasInterviewEnded(MemberInterview memberInterview) {
        return memberInterview.getStatus().equals(InterviewStatus.DONE) || memberInterview.getStatus().equals(InterviewStatus.NO_SHOW);
    }

}