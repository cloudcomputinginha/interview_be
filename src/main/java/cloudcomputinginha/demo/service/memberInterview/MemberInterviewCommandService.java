package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.domain.Coverletter;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.Resume;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;


public interface MemberInterviewCommandService {
    MemberInterview changeMemberInterviewStatus(Long interviewId, Long memberId, InterviewStatus status);

    MemberInterview createMemberInterview(Long interviewId, Long memberId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO);

    void finalizeStatuses(Long interviewId);

    MemberInterview changeMemberInterviewDocument(Long interviewId, Long memberId, MemberInterviewRequestDTO.updateDocumentDTO updateDocumentDTO);

    public Coverletter validateCoverletterOwnership(Long coverletterId, Long memberId);

    public Resume validateResumeOwnership(Long resumeId, Long memberId); //TODO: 해당 이력서 및 자소서 검증 로직 객체지향적으로...
}
