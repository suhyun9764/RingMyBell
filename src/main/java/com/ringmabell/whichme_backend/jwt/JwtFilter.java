package com.ringmabell.whichme_backend.jwt;

import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_HEADER;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_PREFIX;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.JWT_EXPIRED_MS;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.REFRESH_TOKEN_EXPIRED_MS;

import com.ringmabell.whichme_backend.entitiy.Member;
import com.ringmabell.whichme_backend.entitiy.RefreshToken;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getAccessToken(request, request.getHeader(AUTHORIZATION_HEADER));

			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}

			// 토큰 만료 체크
			if (jwtUtil.isExpired(token)) {
				token = handleExpiredToken(request, response);

			}

			setSecurityContext(token);
			filterChain.doFilter(request, response);

		} catch (AuthenticationException e) {
			log.error("Authentication failed: {}", e.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
		} catch (Exception e) {
			log.error("Error in JWT processing: {}", e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
		}
	}

	private String handleExpiredToken(HttpServletRequest request, HttpServletResponse response) {
		String token;
		String refreshToken = getRefreshToken(request);

		// Refresh Token 존재 여부 확인
		checkExistRefreshToken(refreshToken);
		checkRefreshTokenIsMatched(refreshToken);

		String username = jwtUtil.getUsername(refreshToken);
		String role = jwtUtil.getRole(refreshToken);

		// 새로운 Access Token 및 Refresh Token 발급
		String newAccessToken = jwtUtil.createJwt(username, role, JWT_EXPIRED_MS);
		String newRefreshToken = jwtUtil.createRefreshToken(username, role, REFRESH_TOKEN_EXPIRED_MS);

		// Refresh Token 업데이트
		refreshTokenRepository.deleteById(username);
		refreshTokenRepository.save(new RefreshToken(username, newRefreshToken));

		// Access Token과 Refresh Token을 응답에 추가
		response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + newAccessToken);
		setRefreshTokenCookie(response, newRefreshToken);
		token = newAccessToken;

		log.info("[JWT EXPIRED] : new AccessToken ={}, RefreshToken = {}", newAccessToken, newRefreshToken);
		return token;
	}

	private String getAccessToken(HttpServletRequest request, String authorization) {
		if (authorization != null && authorization.startsWith(AUTHORIZATION_PREFIX)) {
			return authorization.substring(AUTHORIZATION_PREFIX.length());
		}

		// 쿠키에서 Access Token 확인
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> "Authorization".equals(cookie.getName()))
				.map(Cookie::getValue)
				.findFirst())
			.orElse(null);
	}

	private String getRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> "Refresh-Token".equals(cookie.getName()))
				.map(Cookie::getValue)
				.findFirst())
			.orElseThrow(() -> new AuthenticationException("로그인 만료") {
			});
	}

	private void checkExistRefreshToken(String refreshToken) {
		if (refreshToken == null || !refreshTokenRepository.existsById(jwtUtil.getUsername(refreshToken))) {
			log.error("Refresh token is null or not in the database");
			throw new AuthenticationException("로그인 만료") {
			};
		}
	}

	private void checkRefreshTokenIsMatched(String refreshToken) {
		RefreshToken storedToken = refreshTokenRepository.findById(jwtUtil.getUsername(refreshToken)).get();
		if (!refreshToken.equals(storedToken.getRefreshToken())) {
			log.error("Refresh token mismatch");
			throw new AuthenticationException("로그인 만료") {
			};
		}
	}

	private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		Cookie refreshTokenCookie = new Cookie("Refresh-Token", refreshToken);
		refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
		refreshTokenCookie.setSecure(true); // HTTPS에서만 전송 (추후 주석 해제)
		refreshTokenCookie.setPath("/"); // 모든 경로에서 사용 가능
		refreshTokenCookie.setMaxAge((int)(REFRESH_TOKEN_EXPIRED_MS / 1000)); // 쿠키 유효기간 설정
		response.addCookie(refreshTokenCookie);
	}

	private void setSecurityContext(String token) {
		CustomUserDetails customUserDetails = createUserDetails(token);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());

		// SecurityContextHolder에 인증 정보 저장
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	private CustomUserDetails createUserDetails(String token) {
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		Member member = Member.builder()
			.loginId(username)
			.role(Role.valueOf(role))
			.build();

		return new CustomUserDetails(member);
	}
}
