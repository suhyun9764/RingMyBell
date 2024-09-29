package com.ringmabell.whichme_backend.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ringmabell.whichme_backend.exception.errorcode.ExpiredTokenException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	private SecretKey secretKey;

	// 암호화, 복호화에 사용되는 비밀키
	public JwtUtil(@Value("${spring.jwt.secretKey}") String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("username")
				.toString();
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("로그인 만료");
		}

	}

	public String getRole(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role")
				.toString();
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("로그인 만료");
		}

	}

	public Boolean isExpired(String token) {
		try {
			// 토큰을 파싱하고 만료 시간을 가져옴
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
		} catch (ExpiredJwtException e) {
			// 토큰이 만료되었을 때 발생하는 예외 처리
			return true;  // 만료된 경우 true 반환
		} catch (JwtException e) {
			// 다른 JWT 파싱 예외 처리 (예: 잘못된 서명, 잘못된 형식 등)
			System.out.println("Invalid JWT token: " + e.getMessage());
			return true;  // 유효하지 않은 토큰도 만료된 것으로 간주
		}
	}

	public String createJwt(String username, String role, Long expiredMs) {
		return Jwts.builder()
			.claim("username", username)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(String username, String role, Long expiredMS) {
		String token = Jwts.builder()
			.claim("username", username)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMS))
			.signWith(secretKey)
			.compact();

		return token;
	}

}
