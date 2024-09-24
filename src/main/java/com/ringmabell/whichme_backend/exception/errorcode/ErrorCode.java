package com.ringmabell.whichme_backend.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    int getCode();
    String getMessage();
}

