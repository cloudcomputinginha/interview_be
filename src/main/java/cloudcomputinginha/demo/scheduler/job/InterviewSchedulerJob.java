package cloudcomputinginha.demo.scheduler.job;

import cloudcomputinginha.demo.service.interview.InterviewCommandService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewSchedulerJob extends QuartzJobBean {

    private final InterviewCommandService interviewCommandService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Long interviewId = dataMap.getLong("interviewId");
        Long memberId = dataMap.getLong("memberId");

        interviewCommandService.startInterview(memberId, interviewId, true);
    }
}