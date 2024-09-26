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

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			LoginDto loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);

			String username = loginDto.getUsername();
			String password = loginDto.getPassword();


			// 로그인 요청한 아이디로 가입된 유저인지 확인
			if(userDetailsService.loadUserByUsername(username)==null) {
				throw new UsernameNotFoundException(INVALID_USERNAME);
			}
			// 아이디와 패스워드를 포함하는 인증 토큰 생성
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				username, password);

			// 인증토큰을 검증하여 유효한 계정이면 Authentication을 반환하고 실패하면 AuthenticationException 발생
			// 반환된 Authentication은 SecurityContextHolder에 저장
			return authenticationManager.authenticate(authToken);

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (UsernameNotFoundException e){
			throw new CustomAuthenticationException(e.getMessage());
		} catch (AuthenticationException e){
			throw new CustomAuthenticationException(INVALID_PASSWORD);
		}
	}

	@Override	// 로그인 검증에 성공하면 유저정보(username,role)와 만료시간을 포함한 jwt발급
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication){
		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		GrantedAuthority auth = authorities.iterator().next();

		String role = auth.getAuthority();

		String token = jwtUtil.createJwt(username,role,JWT_EXPIRED_MS);

		response.addHeader(AUTHORIZATION_HEADER,AUTHORIZATION_PREFIX+token);

	}

	@Override	// 로그인에 실패하면 401과 메세지 반환
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws
		IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(failed.getMessage());
	}

}
