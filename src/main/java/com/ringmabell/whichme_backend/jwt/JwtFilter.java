package com.ringmabell.whichme_backend.jwt;

import static com.ringmabell.whichme_backend.constants.AuthPolicy.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ringmabell.whichme_backend.entitiy.RefreshToken;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.User;
import com.ringmabell.whichme_backend.repository.RefreshTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		String token = getToken(request, authorization);

		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtUtil.isExpired(token)) {
			String refreshToken = getToken(request, request.getHeader("Refresh-Token"));
			if (refreshToken == null || !refreshTokenRepository.existsById(jwtUtil.getUsername(refreshToken))) {
				throw new RuntimeException("로그인 만료");
			}

			String username = jwtUtil.getUsername(refreshToken);
			String role = jwtUtil.getRole(refreshToken);
			String newAccessToken = jwtUtil.createJwt(username, role, JWT_EXPIRED_MS);
			String newRefreshToken = jwtUtil.createRefreshToken(username, role, REFRESH_TOKEN_EXPIRED_MS);
			refreshTokenRepository.deleteById(username);
			refreshTokenRepository.save(new RefreshToken(username, newRefreshToken));

			response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + newAccessToken);
			response.addHeader("Refresh-Token", AUTHORIZATION_PREFIX + newRefreshToken);
			token = newAccessToken;
			log.info("[JWT EXPIRED] : publish new AccessToken ={}, RefreshToken = {}", newAccessToken, newRefreshToken);

		}

		CustomUserDetails customUserDetails = createUserDetails(token);

		UsernamePasswordAuthenticationToken authToken = createAuthToken(
			customUserDetails);

		// jwt가 검증되었기 떄문에 바로 토큰을 SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}

	private static UsernamePasswordAuthenticationToken createAuthToken(
		CustomUserDetails customUserDetails) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());
		return authToken;
	}

	private CustomUserDetails createUserDetails(String token) {
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		User userData = User.builder()
			.username(username)
			.role(Role.valueOf(role))
			.build();

		CustomUserDetails customUserDetails = new CustomUserDetails(userData);
		return customUserDetails;
	}

	private static String getToken(HttpServletRequest request, String authorization) {
		String token = null;

		if (authorization == null) { // 헤더에 jwt가 없을 경우 쿠키 검색
			token = Optional.ofNullable(request.getCookies())
				.flatMap(cookies -> Arrays.stream(cookies)
					.filter(cookie -> "Authorization".equals(cookie.getName()))
					.map(cookie -> cookie.getValue())
					.findFirst())
				.orElse(null);
		} else if (authorization != null || authorization.startsWith("Bearer ")) {
			token = authorization.split(" ")[1];
		}
		return token;
	}
}
