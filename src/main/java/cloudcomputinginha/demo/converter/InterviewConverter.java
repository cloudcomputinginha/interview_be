package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.InterviewOption;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.StartType;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import java.time.LocalDateTime;

public class InterviewConverter {
    public static InterviewResponseDTO.InterviewEndResultDTO toInterviewEndResultDTO(Interview interview) {
        return InterviewResponseDTO.InterviewEndResultDTO.builder()
                .interviewId(interview.getId())
                .endedAt(interview.getEndedAt())
                .build();
    }

    public static InterviewResponseDTO.InterviewCreateResultDTO createInterview(Interview interview) {
        return InterviewResponseDTO.InterviewCreateResultDTO.builder()
                .interviewId(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .corporateName(interview.getCorporateName())
                .jobName(interview.getJobName())
                .interviewFormat(interview.getInterviewOption().getInterviewFormat())
                .interviewType(interview.getInterviewOption().getInterviewType())
                .startType(interview.getStartType())
                .startedAt(interview.getStartedAt())
                .createdAt(interview.getCreatedAt())
                .build();
    }

    public static Interview toInterview(InterviewRequestDTO.InterviewCreateDTO request, InterviewOption interviewOption, Member member) {
        return Interview.builder()
                .name(request.getJobName())
                .description(request.getDescription())
                .corporateName(request.getCorporateName())
                .jobName(request.getJobName())
                .sessionName(request.getSessionName())
                .interviewOption(interviewOption)
                .startType(request.getStartType())
                .startedAt(combineStartAt(request))
                .isOpen(request.getIsOpen() != null ? request.getIsOpen() : false)
                .maxParticipants(request.getMaxParticipants())
                .hostId(member.getId())
                .build();
    }

    public static InterviewOption toInterviewOption(InterviewRequestDTO.InterviewCreateDTO request) {
        return InterviewOption.builder()
                .interviewFormat(request.getInterviewFormat())
                .interviewType(request.getInterviewType())
                .voiceType(request.getVoiceType())
                .questionNumber(request.getQuestionNumber())
                .answerTime(request.getAnswerTime())
                .build();
    }

    private static LocalDateTime combineStartAt(InterviewRequestDTO.InterviewCreateDTO request) {
        if (request.getStartType() == StartType.NOW) {
            return LocalDateTime.now();
        }
        if (request.getScheduledDate() == null || request.getScheduledTime() == null) {
            throw new IllegalArgumentException("예정된 면접의 경우 예정 날짜와 예정 시간이 필요합니다.");
        }
        return LocalDateTime.parse(request.getScheduledDate() + "T" + request.getScheduledTime() + ":00");
    }

    public static InterviewResponseDTO.InterviewGroupCardDTO toInterviewGroupCardDTO(Interview interview, int currentParticipants) {
        return InterviewResponseDTO.InterviewGroupCardDTO.builder()
                .interviewId(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .sessionName(interview.getSessionName())
                .jobName(interview.getJobName())
                .interviewType(interview.getInterviewOption().getInterviewType())
                .currentParticipants(currentParticipants)
                .maxParticipants(interview.getMaxParticipants())
                .startedAt(interview.getStartedAt())
                .build();
    }
}
