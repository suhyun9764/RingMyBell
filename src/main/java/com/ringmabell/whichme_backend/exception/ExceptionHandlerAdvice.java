package com.ringmabell.whichme_backend.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.common.CommonErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.user.UserErrorCode;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("[Exception] cause : {], message:{}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("[IllegalArgumentException] cause : {}, message:{}", NestedExceptionUtils.getMostSpecificCause(e),
			e.getMessage());
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			String.format("%s %s", errorCode.getMessage(),
				NestedExceptionUtils.getMostSpecificCause(e).getMessage()));
		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("[MethodArgumentNotValidException] cause : {}, message:{}",
			NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
		ErrorCode errorCode = CommonErrorCode.INVALID_ARGUMENT_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			errorCode.getMessage(),
			e.getBindingResult());

		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("[HttpMessageNotReadableException] cause: {}, message: {}",
			NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
		ErrorCode errorCode = CommonErrorCode.INVALID_FORMAT_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			errorCode.getMessage());
		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
	}

	//중복 회원 예외처리
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity handleDuplicateException(DuplicateException e) {
		log.error("[DuplicateException : Conflict] cause: {}, message: {}",
			NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
		ErrorCode errorCode = UserErrorCode.USER_ALREADY_EXISTS_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			e.getMessage());
		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e) {
		log.error("[EntityNotFoundException] cause:{}, message: {}", NestedExceptionUtils.getMostSpecificCause(e),
			e.getMessage());
		ErrorCode errorCode = UserErrorCode.USER_NOT_FOUND_ERROR;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
			errorCode.getMessage());
		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
	}

}
