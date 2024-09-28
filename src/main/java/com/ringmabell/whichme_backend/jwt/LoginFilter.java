package com.ringmabell.whichme_backend.jwt;

import static com.ringmabell.whichme_backend.constants.AuthPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.dto.LoginDto;
import com.ringmabell.whichme_backend.exception.CustomAuthenticationException;
import com.ringmabell.whichme_backend.response.Response;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
			String username = loginDto.getUsername();
			String password = loginDto.getPassword();

			validateUser(username);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (UsernameNotFoundException e) {
			throw new CustomAuthenticationException(e.getMessage());
		} catch (AuthenticationException e) {
			throw new CustomAuthenticationException(INVALID_PASSWORD);
		}
	}

	private void validateUser(String username) {
		if (userDetailsService.loadUserByUsername(username) == null) {
			throw new UsernameNotFoundException(INVALID_USERNAME);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain, Authentication authentication) throws IOException {
		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
		String username = customUserDetails.getUsername();
		String role = getUserRole(authentication);

		String token = jwtUtil.createJwt(username, role, JWT_EXPIRED_MS);
		response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + token);
		writeJsonResponse(response, Response.builder()
			.success(true)
			.message(COMPLETE_LOGIN)
			.build());
	}

	private String getUserRole(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		writeJsonResponse(response, Response.builder()
			.success(false)
			.message(failed.getMessage())
			.build());
	}

	private void writeJsonResponse(HttpServletResponse response, Response responseData) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String jsonResponse = objectMapper.writeValueAsString(responseData);
		response.getWriter().write(jsonResponse);
	}
}
