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

    /**
     * interviewId와 memberId에 해당하는 면접을 시스템에서 자동으로 시작시킨다.
     *
     * @param context
     */
    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Long interviewId = dataMap.getLong("interviewId");
        Long memberId = dataMap.getLong("memberId");

        interviewCommandService.startInterview(memberId, interviewId, true);
    }
}