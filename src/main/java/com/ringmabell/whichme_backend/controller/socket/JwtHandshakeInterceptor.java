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
		String authHeader = headers.getFirst("Authorization");  // Authorization 헤더 가져오기

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			// JWT 토큰이 없거나 잘못된 경우, HTTP 401 Unauthorized 상태 설정
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;  // 핸드셰이크 실패
		}

		String token = authHeader.substring(7);  // "Bearer " 이후의 실제 JWT 토큰 추출

		// JWT 검증 로직 추가
		if (!isValidToken(token)) {
			// 토큰이 유효하지 않은 경우, HTTP 403 Forbidden 상태 설정
			response.setStatusCode(HttpStatus.FORBIDDEN);
			return false;  // 핸드셰이크 실패
		}

		// 토큰이 유효하면 사용자 정보를 설정
		CustomUserDetails userDetails = createUserDetails(token);

		boolean isDispatch = userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(role -> role.equals("ROLE_DISPATCH"));

		if (!isDispatch) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;  // 핸드셰이크 실패
		}

		attributes.put("userDetails", userDetails);
		return true;  // 핸드셰이크 성공
	}

	// 토큰 유효성 검증 로직
	private boolean isValidToken(String token) {
		try {
			// 토큰이 만료되었는지 확인
			if (jwtUtil.isExpired(token)) {
				return false;
			}

			// 추가 검증을 원할 경우 추가 가능
			String username = jwtUtil.getUsername(token);
			return username != null;
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
			.role(Role.valueOf(role))  // Role enum을 사용하여 역할 설정
			.build();

		return new CustomUserDetails(member);
	}
}
