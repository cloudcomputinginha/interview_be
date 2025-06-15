package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;

import java.util.List;
import java.util.Map;

public class MemberInterviewConverter {
    public static MemberInterviewResponseDTO.MemberInterviewStatusDTO toMemberInterviewStatusDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.MemberInterviewStatusDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .status(memberInterview.getStatus())
                .updatedAt(memberInterview.getUpdatedAt())
                .build();
    }

    public static MemberInterview toMemberInterview(Member member, Interview interview, Resume resume, Coverletter coverletter) {
        return MemberInterview.builder()
                .member(member)
                .interview(interview)
                .resume(resume)
                .coverletter(coverletter)
                .build();
    }

    public static MemberInterviewResponseDTO.CreateMemberInterviewDTO toMemberInterviewResultDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.CreateMemberInterviewDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .createdAt(memberInterview.getCreatedAt())
                .build();
    }

    public static MemberInterviewResponseDTO.ParticipantDTO toParticipantDTO(MemberInterview memberInterview, List<Qna> qnas) {
        return MemberInterviewResponseDTO.ParticipantDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .resumeDTO(ResumeConverter.toResumeSimpleDTO(memberInterview.getResume()))
                .coverLetterDTO(CoverletterConverter.toDetailDTO(memberInterview.getCoverletter(), qnas))
                .build();
    }

    //참여자의 자기소개서 ID를 꺼내서 coverletterID에 해당하는 QNA 리스트를 가져온다.(해당 키가 없으면 빈 리스트 반환)
    //반환된 QNA 리스트와 참여자 정보를 기반으로 응답용 DTO 생성
    public static List<MemberInterviewResponseDTO.ParticipantDTO> toParticipantDTOs(List<MemberInterview> memberInterviews, Map<Long, List<Qna>> qnaMap) {
        return memberInterviews.stream()
                .map(mi -> {
                    Long coverletterId = mi.getCoverletter().getId();
                    List<Qna> qnas = qnaMap.getOrDefault(coverletterId, List.of());
                    return MemberInterviewConverter.toParticipantDTO(mi, qnas);
                }).toList();
    }

    public static MemberInterviewResponseDTO.MyInterviewListDTO toMyInterviewListDTO(List<MemberInterview> myInterviews) {
        List<MemberInterviewResponseDTO.MyInterviewDTO> myInterviewDTOS = myInterviews.stream()
                .map(MemberInterviewConverter::toMyInterviewDTO)
                .toList();
        return new MemberInterviewResponseDTO.MyInterviewListDTO(myInterviewDTOS);
    }

    public static MemberInterviewResponseDTO.MyInterviewDTO toMyInterviewDTO(MemberInterview memberInterview) {
        Interview interview = memberInterview.getInterview();
        InterviewOption option = interview.getInterviewOption();

        return MemberInterviewResponseDTO.MyInterviewDTO.builder()
                .myInterviewCardDTO(InterviewConverter.toInterviewCardDTO(interview))
                .interviewOptionPreviewDTO(InterviewOptionConverter.toInterviewOptionPreviewDTO(option))
                .memberInterviewStatusDTO(MemberInterviewConverter.toMemberInterviewStatusDTO(memberInterview))
                .build();
    }

    public static MemberInterviewResponseDTO.MemberInterviewDocumentDTO toMemberInterviewDocumentDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.MemberInterviewDocumentDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .coverLetterId(memberInterview.getCoverletter().getId())
                .resumeId(memberInterview.getResume().getId())
                .updatedAt(memberInterview.getUpdatedAt())
                .build();
    }
}
