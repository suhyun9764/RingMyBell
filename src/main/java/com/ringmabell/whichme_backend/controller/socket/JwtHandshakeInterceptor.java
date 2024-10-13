package com.ringmabell.whichme_backend.controller.socket;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.ringmabell.whichme_backend.entitiy.Member;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.jwt.CustomUserDetails;
import com.ringmabell.whichme_backend.jwt.JwtUtil;

@Component
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	private final JwtUtil jwtUtil;

	public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
								   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		HttpHeaders headers = request.getHeaders();
		String authHeader = headers.getFirst("Authorization");

		// URL 경로로 소켓 핸들러 구분
		String requestUri = request.getURI().getPath();

		// JWT 토큰 검증
		CustomUserDetails userDetails = validateToken(authHeader, response);
		if (userDetails == null) {
			return false;  // 유효하지 않은 토큰이거나 권한 없음
		}

		// 역할(Role)에 따라 조건적 핸드셰이크
		if (requestUri.contains("/vehicle-socket") && !hasRequiredRole(userDetails, "ROLE_DISPATCH")) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;  // 응급차량 소켓 핸들러에 ROLE_DISPATCH가 아닌 사용자가 접근한 경우
		} else if (requestUri.contains("/user-socket") && !hasRequiredRole(userDetails, "ROLE_USER")) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;  // 일반 사용자 소켓 핸들러에 ROLE_USER가 아닌 사용자가 접근한 경우
		}

		// 사용자 정보를 WebSocket 세션에 저장
		attributes.put("userDetails", userDetails);
		return true;  // 핸드셰이크 성공
	}

	// JWT 토큰을 검증하고 CustomUserDetails 반환
	private CustomUserDetails validateToken(String authHeader, ServerHttpResponse response) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED); // JWT 토큰이 없거나 잘못된 경우
			return null;
		}

		String token = authHeader.substring(7);
		if (!isValidToken(token)) {
			response.setStatusCode(HttpStatus.FORBIDDEN); // 토큰이 유효하지 않은 경우
			return null;
		}

		return createUserDetails(token);
	}

	// 토큰 유효성 검증 로직
	private boolean isValidToken(String token) {
		try {
			return !jwtUtil.isExpired(token) && jwtUtil.getUsername(token) != null;
		} catch (Exception e) {
			return false;
		}
	}

	// 토큰으로부터 사용자 정보를 생성하는 로직
	private CustomUserDetails createUserDetails(String token) {
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		Member member = Member.builder()
				.loginId(username)
				.role(Role.valueOf(role))
				.build();

		return new CustomUserDetails(member);
	}

	// 사용자가 특정 권한을 가지고 있는지 확인
	private boolean hasRequiredRole(CustomUserDetails userDetails, String requiredRole) {
		return userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> role.equals(requiredRole));
	}
}
