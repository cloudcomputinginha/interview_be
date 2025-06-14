package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
