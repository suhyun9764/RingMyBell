package com.ringmabell.whichme_backend.controller.socket;

import com.ringmabell.whichme_backend.jwt.JwtUtil;
import com.ringmabell.whichme_backend.service.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final JwtUtil jwtUtil;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final GeoService geoService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new VehicleWebSocketHandler(geoService), "/vehicle-socket")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor);

        registry.addHandler(new UserWebSocketHandler(geoService), "/user-socket")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor);
    }
}