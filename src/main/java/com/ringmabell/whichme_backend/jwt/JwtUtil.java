package com.ringmabell.whichme_backend.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	private SecretKey secretKey;

	// 암호화, 복호화에 사용되는 비밀키
	public JwtUtil(@Value("${spring.jwt.secretKey}")String secret){
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token){
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username").toString();
	}

	public String getRole(String token){
		return Jwts	.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role").toString();
	}

	public Boolean isExpired(String token){
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}

	public String createJwt(String username, String role, Long expiredMs){
		return Jwts.builder()
			.claim("username",username)
			.claim("role",role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis()+expiredMs))
			.signWith(secretKey)
			.compact();
	}
}
