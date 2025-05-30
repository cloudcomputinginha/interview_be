package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class DocumentHandler extends GeneralException {
    public DocumentHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
