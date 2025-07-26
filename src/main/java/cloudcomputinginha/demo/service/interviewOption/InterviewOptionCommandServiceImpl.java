package cloudcomputinginha.demo.service.interviewOption;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.InterviewOptionConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.repository.InterviewRepository;
import cloudcomputinginha.demo.web.dto.InterviewOptionRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InterviewOptionCommandServiceImpl implements InterviewOptionCommandService {

    private final InterviewRepository interviewRepository;

    @Override
    @Transactional
    public InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO updateInterviewOption(Long memberId, Long interviewId, InterviewOptionRequestDTO.InterviewOptionUpdateDTO request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        if (!interview.getHost().getId().equals(memberId)) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_NO_PERMISSION);
        }

        if (interview.getStartedAt().isBefore(LocalDateTime.now())) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }

        interview.getInterviewOption().updateVoiceType(request.getVoiceType());
        interview.getInterviewOption().updateQuestionNumber(request.getQuestionNumber());
        interview.getInterviewOption().updateAnswerTime(request.getAnswerTime());

        return InterviewOptionConverter.toInterviewOptionUpdateResponseDTO(interview.getInterviewOption());
    }
}
