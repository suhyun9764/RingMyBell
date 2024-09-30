package com.ringmabell.whichme_backend.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.response.ErrorResponse;
import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.user.UserErrorCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorResponse errorResponse = setExpiredException();
        setResponse(response, errorResponse);
    }

    private static ErrorResponse setExpiredException() {
        ErrorCode errorCode = UserErrorCode.EXPIRED_TOKEN_EXCEPTION;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
                errorCode.getMessage());
        return errorResponse;
    }

    private static void setResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);
    }
}