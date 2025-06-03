package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberInterviewQueryServiceImpl implements MemberInterviewQueryService {
    private final MemberInterviewRepository memberInterviewRepository;

    @Override
    public List<MemberInterview> getMyInterviews(Long memberId) {
        return memberInterviewRepository.findAllForMyPage(memberId);
    }
}
