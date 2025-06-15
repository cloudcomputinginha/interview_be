package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.apiPayload.code.handler.DocumentHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.repository.*;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberInterviewCommandServiceImpl implements MemberInterviewCommandService {
    private final MemberInterviewRepository memberInterviewRepository;
    private final MemberRepository memberRepository;
    private final InterviewRepository interviewRepository;
    private final CoverletterRepository coverletterRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public MemberInterview changeMemberInterviewStatus(Long interviewId, Long memberId, InterviewStatus status) {

        // memberId가 존재하는지 확인(방어 코드)
        memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        memberInterview.updateStatus(status);

        return memberInterview;
    }

    @Override
    public MemberInterview createMemberInterview(Long interviewId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO) {

        Long memberId = createMemberInterviewDTO.getMemberId();

        memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        boolean alreadyExists = memberInterviewRepository.existsByMemberIdAndInterviewId(createMemberInterviewDTO.getMemberId(), interviewId);
        if (alreadyExists) {
            throw new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_ALREADY_EXISTS);
        }

        Member member = memberRepository.getReferenceById(createMemberInterviewDTO.getMemberId());
        Interview interview = interviewRepository.getReferenceById(interviewId);

        if (!interview.getIsOpen()) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_NOT_ACCEPTING_MEMBERS);
        }

        Resume resume = validateResumeOwnership(createMemberInterviewDTO.getResumeId(), memberId);
        Coverletter coverletter = validateCoverletterOwnership(createMemberInterviewDTO.getCoverletterId(), memberId);

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        interview.increaseCurrentParticipants(); //이 메서드 내부에서 동시성을 보호하고, 정원이 넘치면 예외를 발생시킵니다.
        return memberInterviewRepository.save(memberInterview);
    }

    @Override
    public void finalizeStatuses(Long interviewId) {
        List<MemberInterview> memberInterviews = memberInterviewRepository.findByInterviewId(interviewId);

        for (MemberInterview mi : memberInterviews) {
            switch (mi.getStatus()) {
                case SCHEDULED -> mi.updateStatus(InterviewStatus.NO_SHOW);
                case IN_PROGRESS -> mi.updateStatus(InterviewStatus.DONE);
                default -> {
                }
            }
        }
    }

    @Override
    public MemberInterview changeMemberInterviewDocument(Long interviewId, MemberInterviewRequestDTO.updateDocumentDTO updateDocumentDTO) {
        Long memberId = updateDocumentDTO.getMemberId();

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        if (memberInterview.getStatus() == InterviewStatus.DONE) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }

        Resume resume = validateResumeOwnership(updateDocumentDTO.getResumeId(), memberId);
        Coverletter coverletter = validateCoverletterOwnership(updateDocumentDTO.getCoverletterId(), memberId);
        memberInterview.updateDocument(resume, coverletter);

        memberInterviewRepository.save(memberInterview);
        return memberInterview;
    }

    private Coverletter validateCoverletterOwnership(Long coverletterId, Long memberId) {
        Coverletter coverletter = coverletterRepository.getReferenceById(coverletterId);
        if (!coverletter.getMember().getId().equals(memberId)) {
            throw new DocumentHandler(ErrorStatus.COVERLETTER_NOT_OWNED);
        }
        return coverletter;
    }

    private Resume validateResumeOwnership(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.getReferenceById(memberId);
        if (!resume.getMember().getId().equals(memberId)) {
            throw new DocumentHandler(ErrorStatus.RESUME_NOT_OWNED);
        }
        return resume;
    }

}
