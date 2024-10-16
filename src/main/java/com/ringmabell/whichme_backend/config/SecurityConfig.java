package com.ringmabell.whichme_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ringmabell.whichme_backend.exception.handler.JwtAccessDenyHandler;
import com.ringmabell.whichme_backend.exception.handler.JwtAuthenticationEntryPoint;
import com.ringmabell.whichme_backend.jwt.CustomUserDetailsService;
import com.ringmabell.whichme_backend.jwt.JwtFilter;
import com.ringmabell.whichme_backend.jwt.JwtUtil;
import com.ringmabell.whichme_backend.jwt.LoginFilter;
import com.ringmabell.whichme_backend.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtAuthenticationEntryPoint entryPoint;
	private final JwtAccessDenyHandler accessDenyHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 로그인 관련 기능 비활성화
		http
			.csrf((auth) -> auth.disable())// 필요 시 CSRF 보호 비활성화
			.formLogin((auth) -> auth.disable())  // 기본 로그인 폼 비활성화
			.httpBasic((auth) -> auth.disable());

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/api/user/**", "/api-test", "/api/dispatch/join/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/api/test/**").hasRole("USER")
				.anyRequest().authenticated());

		http
				.cors((cors) -> cors
						.configurationSource(new CorsConfigurationSource() {
							@Override
							public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
								CorsConfiguration corsConfiguration = new CorsConfiguration();

								corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
								corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
								corsConfiguration.setAllowCredentials(true);
								corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
								corsConfiguration.setMaxAge(3600L);

								corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));


								return corsConfiguration;
							}
						}));

		// 로그인 필터(성공시 jwt 발급)후 jwt 검증
		http
			.addFilterAfter(new JwtFilter(jwtUtil, refreshTokenRepository), LoginFilter.class);

		http
			.exceptionHandling(
				handler -> handler
					.authenticationEntryPoint(entryPoint)   // 인증 작업 예외처리
					.accessDeniedHandler(accessDenyHandler));   // 인가 작업 예외처리

		LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
			userDetailsService, refreshTokenRepository);
		loginFilter.setFilterProcessesUrl("/api/auth/login");

		// UsernamePasswordAuthenticationFilter 대신 custom한 LoginFilter 적용
		http
			.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

		// JWT 방식이므로 서버에서 클라이언트의 세션 정보를 저장하지 않음
		// 클라이언트는 매 요청 시 JWT를 포함해야 하며, 서버는 이를 검증하여 인증을 처리함

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();  // 비밀번호 암호화에 사용할 BCryptPasswordEncoder
	}
}
