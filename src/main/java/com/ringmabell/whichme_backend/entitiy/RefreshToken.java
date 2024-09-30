package com.ringmabell.whichme_backend.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RedisHash(value = "username", timeToLive = 60)
@Getter
@AllArgsConstructor
public class RefreshToken {
	@Id
	private String username;
	private String refreshToken;

}
