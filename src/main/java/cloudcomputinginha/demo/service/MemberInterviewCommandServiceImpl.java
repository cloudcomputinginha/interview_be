package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.apiPayload.code.handler.DocumentHandler;
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
    public MemberInterview changeMemberInterviewStatus(Long interviewId, MemberInterviewRequestDTO.changeMemberStatusDTO memberInterviewRequestDTO) {
        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberInterviewRequestDTO.getMemberId(), interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        memberInterview.changeStatus(memberInterviewRequestDTO.getStatus());
        memberInterviewRepository.save(memberInterview);
        return memberInterview;
    }

    @Override
    public MemberInterview createMemberInterview(Long interviewId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO) {
        boolean alreadyExists = memberInterviewRepository.existsByMemberIdAndInterviewId(createMemberInterviewDTO.getMemberId(), interviewId);
        if (alreadyExists) {
            throw new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_ALREADY_EXISTS);
        }

        Member member = memberRepository.getReferenceById(createMemberInterviewDTO.getMemberId());
        Interview interview = interviewRepository.getReferenceById(interviewId);

        if (!interview.getIsOpen()) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_NOT_ACCEPTING_MEMBERS);
        }

        Resume resume = null;
        if (createMemberInterviewDTO.getResumeId() != null) {
            resume = resumeRepository.getReferenceById(createMemberInterviewDTO.getResumeId());
            if (!resume.getMember().getId().equals(createMemberInterviewDTO.getMemberId())) {
                throw new DocumentHandler(ErrorStatus.RESUME_NOT_OWNED);
            }
        }

        Coverletter coverletter = null;
        if (createMemberInterviewDTO.getCoverletterId() != null) {
            coverletter = coverletterRepository.getReferenceById(createMemberInterviewDTO.getCoverletterId());
            if (!coverletter.getMember().getId().equals(createMemberInterviewDTO.getMemberId())) {
                throw new DocumentHandler(ErrorStatus.COVERLETTER_NOT_OWNED);
            }
        }

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        interview.increaseCurrentParticipants(); //이 메서드 내부에서 동시성을 보호하고, 정원이 넘치면 예외를 발생시킵니다.
        return memberInterviewRepository.save(memberInterview);
    }

    @Override
    public void finalizeStatuses(Long interviewId) {
        List<MemberInterview> memberInterviews = memberInterviewRepository.findByInterviewId(interviewId);

        for (MemberInterview mi : memberInterviews) {
            switch (mi.getStatus()) {
                case SCHEDULED -> mi.changeStatus(InterviewStatus.NO_SHOW);
                case IN_PROGRESS -> mi.changeStatus(InterviewStatus.DONE);
                default -> {
                }
            }
        }

    }
}
