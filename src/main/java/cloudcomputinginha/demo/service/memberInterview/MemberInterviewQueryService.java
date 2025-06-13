package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.domain.MemberInterview;

import java.util.List;

public interface MemberInterviewQueryService {
    List<MemberInterview> getMyInterviews(Long memberId);
}
