package cloudcomputinginha.demo.apiPayload.exception;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.code.ErrorReasonDTO;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super("[" + code.getReason().getCode() + "] " + code.getReason().getMessage());  // 에러 메시지 전달
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
