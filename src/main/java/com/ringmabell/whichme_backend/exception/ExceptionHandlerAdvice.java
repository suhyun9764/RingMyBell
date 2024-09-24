package com.ringmabell.whichme_backend.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.common.CommonErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e){
		log.error("[Exception] cause : {], message:{}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
	}



}
