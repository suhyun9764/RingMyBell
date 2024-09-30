package com.ringmabell.whichme_backend.exception.errorcode.user;

import org.springframework.http.HttpStatus;

import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND,404,"존재하지 않는 사용자입니다"),
    USER_ALREADY_EXISTS_ERROR(HttpStatus.CONFLICT,409, "는 이미 존재하는 회원입니다"),
    INACTIVE_USER_ERROR(HttpStatus.FORBIDDEN, 403,"권한이 없는 사용자입니다"),
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED,401,"로그인에 실패하였습니다"),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,401,"사용자 인증이 만료되었습니다. 다시 로그인해 주세요.")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
