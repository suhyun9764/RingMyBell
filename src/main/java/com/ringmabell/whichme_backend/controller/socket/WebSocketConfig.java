package com.ringmabell.whichme_backend.controller.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.ringmabell.whichme_backend.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	private final JwtUtil jwtUtil;
	private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new VehicleWebSocketHandler(), "/vehicle-socket")
			.setAllowedOrigins("*")
			.addInterceptors(jwtHandshakeInterceptor);
	}
}