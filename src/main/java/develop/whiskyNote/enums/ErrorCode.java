package develop.whiskyNote.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    SYSTEM_ERROR(500,"E00001","시스템 에러"),
    JSON_INVALID(400,"E00101", "유효하지 않는 Json형식입니다."),
    REQUEST_BODY_NULL(400,"E00102","요청본문이 비어있습니다."),
    PARAMETER_INVALID_GENERAL(400,"E00103", "유효하지 않는 Parameter가 포함되어 있습니다."),
    TOKEN_UNAUTHORIZED(403,"E00104", "해당 요청에 대한 권한이 없습니다."),
    JWT_ACCESS_INVALID(400,"E00105","유효하지 않은 Access 토큰입니다."),
    JWT_ACCESS_EXPIRED_INVALID(400,"E00106","만료된 Access 토큰입니다."),
    JWT_REFRESH_INVALID(400,"E00107", "유효하지 않은 Refresh 토큰입니다."),
    JWT_REFRESH_EXPIRED_INVALID(400,"E00108","만료된 Refresh 토큰입니다."),
    PARAMETER_INVALID_SPECIFIC(400,"E00109", "%s이(가) 유효하지 않습니다."),
    INPUT_DUPLICATED(400,"E00110","%s이(가) 중복되었습니다."),
    TOKEN_MEMBER_NOT_EXIST(400,"E00111","Token에 해당하는 회원이 존재하지 않습니다."),

    MEMBER_NOT_EXIST(400,"E00201","Request에 해당하는 회원이 존재하지 않습니다."),
    EXISTING_PASSWORD_NOT_MATCH(400,"E00202","현재 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_CHECK(400,"E00203","이메일 인증이 필요합니다.");

    private final int status;
    private final String errorCode;
    private final String errorDescription;
}
