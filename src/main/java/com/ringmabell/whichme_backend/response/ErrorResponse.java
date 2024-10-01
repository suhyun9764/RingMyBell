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

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private final List<ValidationError> errors;

    public static ErrorResponse of(HttpStatus httpStatus,int code, String message){
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
    public static ErrorResponse of(HttpStatus httpStatus,int code, String message, BindingResult bindingResult){
        String combinedMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("Field: %s, Value: %s, Message: %s",
                        fieldError.getField(),
                        fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; ")); // 에러 메시지를 '; '로 구분하여 결합

        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(combinedMessage)
//                .errors(ValidationError.of(bindingResult))
                .build();
    }

//    @Getter
//    public static class ValidationError{
//        private final String field;
//        private final String value;
//        private final String message;
//
//        private ValidationError(FieldError fieldError){
//            this.field = fieldError.getField();
//            this.value = fieldError.getRejectedValue() == null? "" :fieldError.getRejectedValue().toString() ;
//            this.message = fieldError.getDefaultMessage();
//        }
//
//        public static List<ValidationError> of(final BindingResult bindingResult){
//            return bindingResult.getFieldErrors().stream()
//                    .map(ValidationError :: new)
//                    .toList();
//        }
//
//    }

}