package com.ringmabell.whichme_backend.jwt;

import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_HEADER;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.AUTHORIZATION_PREFIX;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.JWT_EXPIRED_MS;
import static com.ringmabell.whichme_backend.constants.AuthPolicy.REFRESH_TOKEN_EXPIRED_MS;
import static com.ringmabell.whichme_backend.constants.UserMessages.COMPLETE_LOGIN;
import static com.ringmabell.whichme_backend.constants.UserMessages.INVALID_PASSWORD;
import static com.ringmabell.whichme_backend.constants.UserMessages.INVALID_USERNAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.dto.LoginDto;
import com.ringmabell.whichme_backend.entitiy.RefreshToken;
import com.ringmabell.whichme_backend.exception.exptions.CustomAuthenticationException;
import com.ringmabell.whichme_backend.repository.RefreshTokenRepository;
import com.ringmabell.whichme_backend.response.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken authToken = createAuthToken(loginDto);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UsernameNotFoundException e) { throw new CustomAuthenticationException(e.getMessage());
        } catch (AuthenticationException e) {
            throw new CustomAuthenticationException(INVALID_PASSWORD);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String loginId = customUserDetails.getUsername();
        String role = getUserRole(authentication);

        deleteExistingRefreshToken(loginId);

        String accessToken = jwtUtil.createJwt(loginId, role, JWT_EXPIRED_MS);
        String refreshToken = jwtUtil.createRefreshToken(loginId, role, REFRESH_TOKEN_EXPIRED_MS);

        saveRefreshToken(loginId, refreshToken);
        setAuthHeaders(response, accessToken, refreshToken);

        writeJsonResponse(response, Response.builder()
                .success(true)
                .message(COMPLETE_LOGIN)
                .build());
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

    private UsernamePasswordAuthenticationToken createAuthToken(LoginDto loginDto) {
        String username = loginDto.getLoginId();
        String password = loginDto.getPassword();
        int loginType = loginDto.getLoginType();

        validateUser(username,loginType);

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private void validateUser(String loginId,int loginType) {
        if(loginType==1){
            if (!userDetailsService.isExistByUsername(loginId)) {
                throw new UsernameNotFoundException(INVALID_USERNAME);
            }
            return;
        }

        if(!userDetailsService.isExistByVehicleNumber(loginId)){
            throw new UsernameNotFoundException(INVALID_USERNAME);
        }

    }

    private String getUserRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
    }

    private void deleteExistingRefreshToken(String username) {
        RefreshToken existingToken = refreshTokenRepository.findByUsername(username);
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }
    }

    private void saveRefreshToken(String username, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(username, refreshToken));
    }

    private void setAuthHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + accessToken);

        Cookie refreshTokenCookie = new Cookie("Refresh-Token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRED_MS / 1000));
        response.addCookie(refreshTokenCookie);
    }

    private void writeJsonResponse(HttpServletResponse response, Response responseData) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(responseData);
        response.getWriter().write(jsonResponse);
    }
}
