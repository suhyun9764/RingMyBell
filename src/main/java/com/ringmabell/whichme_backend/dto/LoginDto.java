package com.ringmabell.whichme_backend.dto;

import lombok.Getter;

@Getter
public class LoginDto {
	private String loginId;
	private String password;
	private int loginType;
}
