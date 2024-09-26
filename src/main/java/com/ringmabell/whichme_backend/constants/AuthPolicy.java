package com.ringmabell.whichme_backend.constants;

public class AuthPolicy {
	public static final Long JWT_EXPIRED_MS = 3600000L;	// 1시간
	public static final String AUTHORIZATION_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
}
