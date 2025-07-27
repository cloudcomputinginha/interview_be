package cloudcomputinginha.demo.infra.messaging;

import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.NotificationDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.NotificationDTO.Type;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.ParticipantsDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingRoomEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberInterviewRepository memberInterviewRepository;

    public void notifyEnter(WaitingRoomActionDTO message) {

        NotificationDTO dto = toNotificationDTO(message.getMemberId(), message.getInterviewId(), NotificationDTO.Type.ENTER);

        messagingTemplate.convertAndSend(
                getWaitingRoomTopic(message.getInterviewId()),
                dto
        );
    }


    public void notifyLeave(WaitingRoomActionDTO message) {
        messagingTemplate.convertAndSend(
                getWaitingRoomTopic(message.getInterviewId()),
                toNotificationDTO(message.getMemberId(), message.getInterviewId(), Type.LEAVE)
        );
    }

    public void broadcastParticipants(Long interviewId) {

        List<Long> participants = memberInterviewRepository.findByInterviewIdAndInprogress(interviewId)
                .stream()
                .map(mi -> mi.getMember().getId())
                .distinct()
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend(
                getParticipantTopic(interviewId),
                toParticipantsDTO(interviewId, participants)
        );
    }

    public void broadcastInterviewStart(Long interviewId) {
        messagingTemplate.convertAndSend(
                getWaitingRoomTopic(interviewId),
                toNotificationDTO(null, interviewId, Type.START)
        );
    }

    private String getParticipantTopic(Long interviewId) {
        return "/topic/waiting-room/" + interviewId + "/participants";
    }


    private String getWaitingRoomTopic(Long interviewId) {
        return "/topic/waiting-room/" + interviewId;
    }


    private NotificationDTO toNotificationDTO(Long memberId, Long interviewId, NotificationDTO.Type type) {
        return NotificationDTO.builder()
                .memberId(memberId)
                .interviewId(interviewId)
                .type(type)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ParticipantsDTO toParticipantsDTO(Long interviewId, List<Long> participants) {
        return ParticipantsDTO.builder()
                .interviewId(interviewId)
                .memberIds(participants)
                .build();
    }

}