package com.ringmabell.whichme_backend.response;

import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.user.UserErrorCode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    @Schema(example = "false")
    private final boolean success = false;
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public static ErrorResponse of(HttpStatus httpStatus,int code, String message){
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
    public static ErrorResponse of(HttpStatus httpStatus,int code,BindingResult bindingResult){
        String combinedMessage = bindingResult.getFieldErrors().stream()
            .findFirst()
            .map(error->error.getDefaultMessage())
            .orElse(null)
            .toString();

        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(combinedMessage)
                .build();
    }

}