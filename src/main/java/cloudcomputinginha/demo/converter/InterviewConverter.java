package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.domain.enums.StartType;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static InterviewResponseDTO.InterviewGroupCardDTO toInterviewGroupCardDTO(Interview interview) {
        return InterviewResponseDTO.InterviewGroupCardDTO.builder()
                .interviewId(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .sessionName(interview.getSessionName())
                .jobName(interview.getJobName())
                .interviewType(interview.getInterviewOption().getInterviewType())
                .currentParticipants(interview.getCurrentParticipants())
                .maxParticipants(interview.getMaxParticipants())
                .startedAt(interview.getStartedAt())
                .build();
    }

    public static InterviewResponseDTO.InterviewDTO toInterviewDTO(Interview interview, int participantCount) {
        return InterviewResponseDTO.InterviewDTO.builder()
                .interviewId(interview.getId())
                .corporateName(interview.getCorporateName())
                .jobName(interview.getJobName())
                .startType(interview.getStartType())
                .participantCount(participantCount)
                .build();
    }

    public static InterviewResponseDTO.InterviewStartResponseDTO toInterviewStartResponseDTO(
            Interview interview,
            List<MemberInterview> memberInterviews,
            List<Qna> allQnas
    ) {
        // 면접 참가자의 모든 QNA를 조회한 후, 자기소개서 id를 바탕으로 조회한 QNA를 그룹핑한다.
        Map<Long, List<Qna>> qnaMap = allQnas.stream()
                .collect(Collectors.groupingBy(q -> q.getCoverletter().getId()));

        InterviewResponseDTO.InterviewDTO interviewDTO = InterviewConverter.toInterviewDTO(interview, memberInterviews.size());
        InterviewOptionResponseDTO.InterviewOptionDetailDTO interviewOptionDTO = InterviewOptionConverter.toInterviewOptionDTO(interview.getInterviewOption());
        List<MemberInterviewResponseDTO.ParticipantDTO> participantDTOs = MemberInterviewConverter.toParticipantDTOs(memberInterviews, qnaMap);

        return InterviewResponseDTO.InterviewStartResponseDTO.builder()
                .interviewId(interview.getId())
                .interview(interviewDTO)
                .options(interviewOptionDTO)
                .participants(participantDTOs)
                .build();
    }

    public static InterviewResponseDTO.GroupInterviewDetailDTO toInterviewGroupDetailDTO(Interview interview, List<MemberInterview> memberInterviewList) {
        return InterviewResponseDTO.GroupInterviewDetailDTO.builder()
                .interviewId(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .sessionName(interview.getSessionName())
                .jobName(interview.getJobName())
                .interviewType(interview.getInterviewOption().getInterviewType())
                .maxParticipants(interview.getMaxParticipants())
                .currentParticipants(interview.getCurrentParticipants())
                .startedAt(interview.getStartedAt())
                .hostName(
                        memberInterviewList.stream()
                                .filter(mi -> mi.getMember().getId().equals(interview.getHostId()))
                                .findFirst()
                                .map(mi -> mi.getMember().getName())
                                .orElse("호스트의 이름을 알 수 없습니다.")
                )
                .groupInterviewParticipants(
                        memberInterviewList.stream()
                                .map(mi -> InterviewResponseDTO.GroupInterviewParticipantDTO.builder()
                                        .memberId(mi.getMember().getId())
                                        .name(mi.getMember().getName())
                                        .isHost(mi.getMember().getId().equals(interview.getHostId()))
                                        .isSubmitted(mi.getResume() != null && mi.getCoverletter() != null)
                                        .build()
                                ).toList()
                )
                .build();
    }

    public static InterviewResponseDTO.InterviewUpdateResponseDTO toInterviewUpdateResponseDTO(Interview interview) {
        return InterviewResponseDTO.InterviewUpdateResponseDTO.builder()
                .interviewId(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .maxParticipants(interview.getMaxParticipants())
                .isOpen(interview.getIsOpen())
                .build();
    }
}
