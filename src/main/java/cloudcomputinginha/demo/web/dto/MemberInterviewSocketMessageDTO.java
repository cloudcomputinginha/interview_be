package cloudcomputinginha.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class MemberInterviewSocketMessageDTO {

    // 입장, 퇴장 알림 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificationDTO {

        private Long memberId;
        private Long interviewId;
        private Type type;
        private LocalDateTime timestamp;

        public enum Type {
            ENTER, LEAVE, START
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WaitingRoomActionDTO {
        private Long memberId;
        private Long interviewId;
        private ActionType action;

        public enum ActionType {
            ENTER, LEAVE
        }
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipantsDTO {
        private Long interviewId;
        private List<Long> memberIds;
    }


    @Builder
    public record SessionInfoDTO(Long memberId, Long interviewId) {}
}
