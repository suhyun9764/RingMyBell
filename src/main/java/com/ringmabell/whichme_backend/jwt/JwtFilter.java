package com.ringmabell.whichme_backend.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
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

		if (token == null || jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		User userData = User.builder()
			.username(username)
			.role(Role.valueOf(role))
			.build();

		CustomUserDetails customUserDetails = new CustomUserDetails(userData);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());

		// jwt가 검증되었기 떄문에 바로 토큰을 SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
}
