package com.ringmabell.whichme_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class JoinDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String realName;
    @NotBlank
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    )
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;

}
