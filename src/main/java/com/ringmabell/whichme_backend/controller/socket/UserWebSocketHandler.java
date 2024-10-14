package com.ringmabell.whichme_backend.controller.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.dto.DispatchPositionDto;
import com.ringmabell.whichme_backend.dto.LocationDto;
import com.ringmabell.whichme_backend.jwt.CustomUserDetails;
import com.ringmabell.whichme_backend.service.GeoService;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
@RequiredArgsConstructor
public class UserWebSocketHandler extends TextWebSocketHandler {

    public static ConcurrentHashMap<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final GeoService geoService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

        boolean isUser = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_USER"));

        if (!isUser) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("권한이 없습니다"));
        } else {
            String username = userDetails.getUsername();
            userSessions.put(username, session);
            System.out.println("유저" + username + "연결 성공");
            super.afterConnectionEstablished(session);

        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = getUsernameFromSession(session);
        if (username != null) {
            System.out.println("차량 " + username + "가 서버에 위치를 전송 중...");
            ObjectMapper objectMapper = new ObjectMapper();
            LocationDto locationDto = objectMapper.readValue(message.getPayload(),
                    LocationDto.class);

            geoService.addUserLocation(username, locationDto.getLatitude(),
                    locationDto.getLongitude());

        } else {
            System.out.println("차량 ID를 찾을 수 없음.");
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = getUsernameFromSession(session);
        if (username != null) {
            userSessions.remove(username);
            System.out.println("유저 " + username + " 연결 종료됨.");
        }
    }

    private String getUsernameFromSession(WebSocketSession session) {
        // WebSocket 세션에서 차량 ID를 조회
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

        if (userDetails != null) {
            return userDetails.getUsername(); // 차량 ID를 username으로 사용
        }
        return null;
    }
}
