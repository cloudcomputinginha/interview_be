package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class MemberInterviewHandler extends GeneralException {
    public MemberInterviewHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
