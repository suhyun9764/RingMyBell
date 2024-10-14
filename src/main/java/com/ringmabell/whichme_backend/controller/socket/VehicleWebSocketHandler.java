package com.ringmabell.whichme_backend.controller.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringmabell.whichme_backend.dto.DispatchPositionDto;
import com.ringmabell.whichme_backend.jwt.CustomUserDetails;
import com.ringmabell.whichme_backend.service.GeoService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
public class VehicleWebSocketHandler extends TextWebSocketHandler {
    private static ConcurrentHashMap<String, WebSocketSession> vehicleSessions = new ConcurrentHashMap<>();
    private final GeoService geoService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // SecurityContext에서 인증된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

        boolean isDispatch = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_DISPATCH"));

        if (!isDispatch) {
            // 권한이 없으면 연결 종료
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("권한이 없습니다"));
        } else {
            // 세션을 차량 ID와 연동
            String vehicleId = userDetails.getUsername(); // 차량 ID를 username으로 사용
            vehicleSessions.put(vehicleId, session);
            System.out.println("차량 " + vehicleId + " 연결 성공.");
            super.afterConnectionEstablished(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String vehicleId = getVehicleIdFromSession(session);
        if (vehicleId != null) {
            System.out.println("차량 " + vehicleId + "가 서버에 위치를 전송 중...");
            ObjectMapper objectMapper = new ObjectMapper();
            DispatchPositionDto dispatchPositionDto = objectMapper.readValue(message.getPayload(),
                    DispatchPositionDto.class);

            geoService.addVehicleLocation(vehicleId, dispatchPositionDto.getCurrentLocation().getLatitude(),
                    dispatchPositionDto.getCurrentLocation().getLongitude());
            List<String> nearbyVehicles = geoService.findNearbyVehicles(
                    dispatchPositionDto.getCurrentLocation().getLongitude(),
                    dispatchPositionDto.getCurrentLocation().getLatitude(), 200);
            System.out.println(nearbyVehicles.get(0));
            notifyToUsers(dispatchPositionDto);
            System.out.println(vehicleSessions.size());
        } else {
            System.out.println("차량 ID를 찾을 수 없음.");
        }
    }

    private void notifyToUsers(DispatchPositionDto dispatchPositionDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dispatchPositionDto);

        for (WebSocketSession userSession : UserWebSocketHandler.userSessions.values()) {
            try {
                if (userSession.isOpen()) {
                    userSession.sendMessage(new TextMessage(json));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결이 종료되면 세션 제거
        String vehicleId = getVehicleIdFromSession(session);
        if (vehicleId != null) {
            vehicleSessions.remove(vehicleId);
            System.out.println("차량 " + vehicleId + " 연결 종료됨.");
        }
    }

    private String getVehicleIdFromSession(WebSocketSession session) {
        // WebSocket 세션에서 차량 ID를 조회
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

        if (userDetails != null) {
            return userDetails.getUsername(); // 차량 ID를 username으로 사용
        }
        return null;
    }
}
