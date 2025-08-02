package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.web.dto.MemberInterviewResponseDTO;

import java.util.List;

public class MemberInterviewConverter {
    public static MemberInterviewResponseDTO.MemberInterviewStatusDTO toMemberInterviewStatusDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.MemberInterviewStatusDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .status(memberInterview.getStatus())
                .updatedAt(memberInterview.getUpdatedAt())
                .build();
    }

    public static MemberInterview toMemberInterview(Member member, Interview interview, Resume resume, Coverletter coverletter) {
        MemberInterview memberInterview = MemberInterview.builder()
                .resume(resume)
                .coverletter(coverletter)
                .status(InterviewStatus.SCHEDULED)
                .build();

        memberInterview.setMember(member);
        memberInterview.setInterview(interview);

        member.addMemberInterview(memberInterview);
        interview.addMemberInterview(memberInterview);

        return memberInterview;
    }

    public static MemberInterviewResponseDTO.CreateMemberInterviewDTO toMemberInterviewResultDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.CreateMemberInterviewDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .createdAt(memberInterview.getCreatedAt())
                .build();
    }

    public static MemberInterviewResponseDTO.ParticipantDTO toParticipantDTO(MemberInterview memberInterview) {
        return MemberInterviewResponseDTO.ParticipantDTO.builder()
                .memberInterviewId(memberInterview.getId())
                .resumeDTO(ResumeConverter.toResumeSimpleDTO(memberInterview.getResume()))
                .coverLetterDTO(CoverletterConverter.toDetailDTO(memberInterview.getCoverletter()))
                .build();
    }

    public static List<MemberInterviewResponseDTO.ParticipantDTO> toParticipantDTOs(List<MemberInterview> memberInterviews) {
        return memberInterviews.stream()
                .map(MemberInterviewConverter::toParticipantDTO)
                .toList();
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
