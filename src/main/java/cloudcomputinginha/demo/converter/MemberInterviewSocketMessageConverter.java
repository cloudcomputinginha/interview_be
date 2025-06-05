package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.SessionInfoDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;

public class MemberInterviewSocketMessageConverter {

    public static SessionInfoDTO toSessionInfoDTO(WaitingRoomActionDTO message){
        return SessionInfoDTO.builder()
            .memberId(message.getMemberId())
            .interviewId(message.getInterviewId())
            .build();
    }



}
