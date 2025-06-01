package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class CoverletterHandler extends GeneralException {
    public CoverletterHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
