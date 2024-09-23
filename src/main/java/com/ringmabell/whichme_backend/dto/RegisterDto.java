package com.ringmabell.whichme_backend.dto;

import lombok.Getter;

@Getter
public class RegisterDto {
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String address;

}
