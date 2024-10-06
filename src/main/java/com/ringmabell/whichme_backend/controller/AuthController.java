package com.ringmabell.whichme_backend.controller;

import static com.ringmabell.whichme_backend.constants.AuthSwaggerMessages.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ringmabell.whichme_backend.dto.LoginDto;
import com.ringmabell.whichme_backend.response.ErrorResponse;
import com.ringmabell.whichme_backend.response.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Operation(summary = "로그인 API", description = "사용자의 로그인 정보를 받아 인증을 처리합니다. loginType 1번은 일반 사용자, 2번은 응급차량입니다")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			headers = {
				@Header(name = "Authorization", description = "Access Token", schema = @Schema(type = "string")),
				@Header(name = "Set-Cookie", description = "Refresh Token 쿠키", schema = @Schema(type = "string"))
			},
			content = @Content(mediaType = "application/json",
				examples = {
					@ExampleObject(name = "로그인 성공", value = LOGIN_COMPLETE_EXAMPLE),
				},
				schema = @Schema(implementation = Response.class)
			)
		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(name = "아이디 오류", value = USERNAME_ERROR_EXAMPLE),
					@ExampleObject(name = "비밀번호 오류", value = PASSWORD_ERROR_EXAMPLE),
				},
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
		// 이 메서드는 실제로 호출되지 않으며 Swagger 문서화를 위한 것.
		// 실제 로직은 LoginFilter에서 처리됩니다.

		// 예시 응답 헤더 및 쿠키 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer [Access-Token]");
		Cookie refreshTokenCookie = new Cookie("Refresh-Token", "[Refresh-Token]");

		return ResponseEntity.ok()
			.headers(headers)
			.body("This method is handled by LoginFilter");
	}
}