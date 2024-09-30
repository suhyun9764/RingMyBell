package com.ringmabell.whichme_backend.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.response.ErrorResponse;
import com.ringmabell.whichme_backend.exception.errorcode.ErrorCode;
import com.ringmabell.whichme_backend.exception.errorcode.user.UserErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDenyHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = setForbiddenException();
        setResponse(response, errorResponse);
    }

    private static void setResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);
    }

    private static ErrorResponse setForbiddenException() {
        ErrorCode errorCode = UserErrorCode.INACTIVE_USER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorCode.getCode(),
                errorCode.getMessage());
        return errorResponse;
    }
}
