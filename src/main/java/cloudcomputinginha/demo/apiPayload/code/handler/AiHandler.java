package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class AiHandler extends GeneralException {
    public AiHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
