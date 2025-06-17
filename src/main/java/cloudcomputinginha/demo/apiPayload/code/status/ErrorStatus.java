package cloudcomputinginha.demo.apiPayload.code.status;

import cloudcomputinginha.demo.apiPayload.code.BaseErrorCode;
import cloudcomputinginha.demo.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // 자소서 관련
    COVERLETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "COVERLETTER4001", "해당하는 자기소개서를 찾을 수 없습니다."),
    COVERLETTER_NOT_OWNED(HttpStatus.FORBIDDEN, "COVERLETTER4002", "자기소개서는 해당 회원의 소유가 아닙니다."),

    // 이력서 관련
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "RESUME4001", "해당하는 이력서를 찾을 수 없습니다."),
    RESUME_NOT_OWNED(HttpStatus.FORBIDDEN, "RESUME4002", "이력서는 해당 회원의 소유가 아닙니다."),
    RESUME_FILE_TYPE_INVALID(HttpStatus.BAD_REQUEST, "RESUME4003", "이력서는 PDF 파일만 업로드할 수 있습니다."),
    RESUME_FILE_NAME_INVALID(HttpStatus.BAD_REQUEST, "RESUME4004", "파일 이름은 한글, 영문, 숫자, 언더바(_), 대시(-), 공백만 허용하며 .pdf로 끝나야 합니다."),

    // 자소서 + 이력서 관련
    AT_LEAST_ONE_PRESENT_DOCUMENTS(HttpStatus.BAD_REQUEST, "DOCUMENT4001", "이력서 또는 자기소개서 중 하나는 반드시 입력해야 합니다."),

    // 면접 관련 에러
    INTERVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "INTERVIEW4001", "해당하는 인터뷰를 찾을 수 없습니다."),
    INTERVIEW_NOT_ACCEPTING_MEMBERS(HttpStatus.BAD_REQUEST, "INTERVIEW4002", "현재 인터뷰는 참여자를 받지 않습니다."),
    INTERVIEW_END_TIME_INVALID(HttpStatus.CONFLICT, "INTERVIEW4003", "인터뷰의 종료 시간이 시작 시간보다 빠릅니다."),
    INTERVIEW_ALREADY_TERMINATED(HttpStatus.CONFLICT, "INTERVIEW4004", "해당하는 인터뷰는 이미 종료되었습니다."),
    INTERVIEW_NO_PERMISSION(HttpStatus.FORBIDDEN, "INTERVIEW4005", "해당 인터뷰 수정 권한이 없습니다."),

    // 멤버 인터뷰 관련 에러,
    INTERVIEW_STATUS_INVALID(HttpStatus.BAD_REQUEST, "MEMBERINTERVIEW4001", "올바른 인터뷰 상태가 아닙니다."),
    MEMBER_INTERVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBERINTERVIEW4002", "해당하는 사용자 인터뷰를 찾을 수 없습니다."),
    MEMBER_INTERVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBERINTERVIEW4003", "이미 해당 인터뷰에 참여 신청이 완료된 회원입니다."),
    INTERVIEW_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMBERINTERVIEW4004", "인터뷰 정원이 초과되어 더 이상 신청할 수 없습니다."),
    INTERVIEW_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "MEMBERINTERVIEW4005", "인터뷰가 이미 시작되어 입장할 수 없습니다."),

    // URL
    URL_INVALID(HttpStatus.BAD_REQUEST, "URL4001", "URL 형식이 올바르지 않습니다."),

    // 알림 관련 에러
    NOTIFICATION_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION5001", "알림 전송에 실패했습니다."),
    NOTIFICATION_TYPE_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION5002", "알림 타입이 올바르지 않습니다."),
    NOTIFICATION_NOT_OWNED(HttpStatus.FORBIDDEN, "NOTIFICATION4001", "알림은 해당 회원의 소유가 아닙니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public static boolean contains(String name) {
        return Arrays.stream(values()).anyMatch(e -> e.name().equals(name));
    }

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
