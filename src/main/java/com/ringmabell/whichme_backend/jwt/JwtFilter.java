package com.ringmabell.whichme_backend.jwt;

import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_HEADER;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_PREFIX;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.JWT_EXPIRED_MS;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.REFRESH_TOKEN_EXPIRED_MS;

import com.ringmabell.whichme_backend.entitiy.RefreshToken;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.User;
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
            String authorization = request.getHeader("Authorization");
            String token = getAccessToken(request, authorization);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtil.isExpired(token)) {
                String refreshToken = getRefreshToken(request);
                if (refreshToken == null || !refreshTokenRepository.existsById(jwtUtil.getUsername(refreshToken))) {
                    log.error("Refresh is null or not in db");
                    throw new AuthenticationException("로그인 만료") {
                    };
                }

                if (!refreshToken.equals(
                        refreshTokenRepository.findById(jwtUtil.getUsername(refreshToken)).get().getRefreshToken())) {
                    log.error("RefreshToken is not matched");
                    throw new AuthenticationException("로그인 만료") {
                    };
                }

                String username = jwtUtil.getUsername(refreshToken);
                String role = jwtUtil.getRole(refreshToken);
                String newAccessToken = jwtUtil.createJwt(username, role, JWT_EXPIRED_MS);
                String newRefreshToken = jwtUtil.createRefreshToken(username, role, REFRESH_TOKEN_EXPIRED_MS);
                refreshTokenRepository.deleteById(username);
                refreshTokenRepository.save(new RefreshToken(username, newRefreshToken));

                response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + newAccessToken);

                Cookie refreshTokenCookie = new Cookie("Refresh-Token", newRefreshToken);
                refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
                // refreshTokenCookie.setSecure(true); // HTTPS에서만 전송 (추후 주석 해제)
                refreshTokenCookie.setPath("/"); // 애플리케이션의 모든 경로에서 사용 가능
                refreshTokenCookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRED_MS / 1000)); // 쿠키 유효기간 설정
                response.addCookie(refreshTokenCookie);
                token = newAccessToken;
                log.info("[JWT EXPIRED] : publish new AccessToken ={}, RefreshToken = {}", newAccessToken,
                        newRefreshToken);
            }

            CustomUserDetails customUserDetails = createUserDetails(token);
            UsernamePasswordAuthenticationToken authToken = createAuthToken(customUserDetails);

            // jwt가 검증되었기 때문에 바로 토큰을 SecurityContextHolder에 저장
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error in JWT processing: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.error("Not found refresh token in cookies");
            throw new AuthenticationException("로그인 만료") {
            };
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "Refresh-Token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("로그인 만료") {
                });
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

    private static String getAccessToken(HttpServletRequest request, String authorization) {
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
