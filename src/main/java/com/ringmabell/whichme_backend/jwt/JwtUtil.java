package com.ringmabell.whichme_backend.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
    private SecretKey secretKey;

    // 암호화, 복호화에 사용되는 비밀키
    public JwtUtil(@Value("${spring.jwt.secretKey}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return getClaim(token, "username");

    }

    public String getRole(String token) {
        return getClaim(token, "role");

    }

    private String getClaim(String token, String username) {
        return getString(token, username);
    }

    private String getString(String token, String username) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(username)
                    .toString();
        } catch (ExpiredJwtException e) {
            log.error("RefreshToken is Expired");
            throw new AuthenticationException("로그인 만료") {
            };
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
