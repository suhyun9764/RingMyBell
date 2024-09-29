package com.ringmabell.whichme_backend.constants;

public class AuthPolicy {
	public static final Long JWT_EXPIRED_MS = 6 * 1000L;
	public static long REFRESH_TOKEN_EXPIRED_MS = 6 * 1000L;	// 추 후 2주로 변경
	public static final String AUTHORIZATION_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
}
