package cloudcomputinginha.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DomainInfo {
    public static String INTERVIEW;
    public static String INTERVIEW_AI;
    private String interview;
    private String interviewAi;


    @Value("${notification.domain.interview}")
    public void setInterview(String interview) {
        DomainInfo.INTERVIEW = interview;
    }

    @Value("${notification.domain.interview-ai}")
    public void setInterviewAi(String interviewAi) {
        DomainInfo.INTERVIEW_AI = interviewAi;
    }
}
