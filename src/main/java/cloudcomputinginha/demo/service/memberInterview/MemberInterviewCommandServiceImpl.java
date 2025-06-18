package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.apiPayload.code.handler.DocumentHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.repository.*;
import cloudcomputinginha.demo.service.notification.NotificationCommandService;
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
    private final NotificationCommandService notificationCommandService;

    @Override
    public MemberInterview changeMemberInterviewStatus(Long interviewId, Long memberId, InterviewStatus status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        memberInterview.updateStatus(status);

        return memberInterview;
    }

    @Override
    public MemberInterview createMemberInterview(Long interviewId, Long memberId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        boolean alreadyExists = memberInterviewRepository.existsByMemberIdAndInterviewId(memberId, interviewId);
        if (alreadyExists) {
            throw new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_ALREADY_EXISTS);
        }

        Interview interview = interviewRepository.getReferenceById(interviewId);
        if (!interview.getIsOpen()) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_NOT_ACCEPTING_MEMBERS);
        }

        Resume resume = validateResumeOwnership(createMemberInterviewDTO.getResumeId(), memberId);
        Coverletter coverletter = validateCoverletterOwnership(createMemberInterviewDTO.getCoverletterId(), memberId);

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        interview.increaseCurrentParticipants(); //이 메서드 내부에서 동시성을 보호하고, 정원이 넘치면 예외를 발생시킵니다.

        // 기존 참가자 목록 조회 (자기 자신 제외)
        List<MemberInterview> otherParticipants = memberInterviewRepository.findByInterviewId(interviewId).stream()
                .filter(mi -> !mi.getMember().getId().equals(memberId))
                .toList();

        // 알림 메시지 생성
        NotificationType notificationType = NotificationType.ROOM_ENTRY;
        String message = notificationType.generateMessage(member.getName(), interview.getName());
        String url = notificationType.generateUrl(interview.getId());
        System.out.println("message = " + message);
        System.out.println("url = " + url);

        // 알림 전송 (비동기 + AFTER_COMMIT)
        otherParticipants.forEach(participant ->
                notificationCommandService.createNotificationAndSend(
                        participant.getMember(),
                        NotificationType.ROOM_ENTRY,
                        message,
                        url
                )
        );

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
    public MemberInterview changeMemberInterviewDocument(Long interviewId, Long memberId, MemberInterviewRequestDTO.updateDocumentDTO updateDocumentDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        if (memberInterview.getStatus() == InterviewStatus.DONE) {
            throw new MemberInterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }

        Resume resume = resumeRepository.getReferenceById(updateDocumentDTO.getResumeId());
        resume.validateOwnedBy(memberId);

        Coverletter coverletter = coverletterRepository.getReferenceById(updateDocumentDTO.getCoverletterId());
        coverletter.validateOwnedBy(memberId);

        memberInterview.updateDocument(resume, coverletter);

        memberInterviewRepository.save(memberInterview);
        return memberInterview;
    }

    public Coverletter validateCoverletterOwnership(Long coverletterId, Long memberId) {
        Coverletter coverletter = coverletterRepository.getReferenceById(coverletterId);
        if (!coverletter.getMember().getId().equals(memberId)) {
            throw new DocumentHandler(ErrorStatus.COVERLETTER_NOT_OWNED);
        }
        return coverletter;
    }

    public Resume validateResumeOwnership(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.getReferenceById(resumeId);
        if (!resume.getMember().getId().equals(memberId)) {
            throw new DocumentHandler(ErrorStatus.RESUME_NOT_OWNED);
        }
        return resume;
    }

}
