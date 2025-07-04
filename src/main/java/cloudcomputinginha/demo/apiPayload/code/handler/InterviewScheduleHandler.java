package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class InterviewScheduleHandler extends GeneralException {
    public InterviewScheduleHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
