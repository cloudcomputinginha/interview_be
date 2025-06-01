package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class ResumeHandler extends GeneralException {
    public ResumeHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
