package com.ringmabell.whichme_backend.constants;

import org.springframework.security.core.parameters.P;

public class UserMessages {
    public static final String USERNAME_POLICY_MESSAGE = "아이디는 3자 이상 20자 이하여야합니다";
    public static final String PASSWORD_POLICY_MESSAGE = "비밀번호는 영문, 숫자, 특수문자(@,$,!,%,*,?,&)를 포함 8글자 이상 16자 이하여야합니다";
    public static final String NOT_BLANK_MESSAGE = "공백일 수 없습니다";
    public static final String EMAIL_POLICY_MESSAGE = "올바른 이메일 형식이 아닙니다";
    public static final String ALREADY_EXIST_USERNAME = "이미 존재하는 아이디입니다";
    public static final String ALREADY_EXIST_EMAIL = "이미 존재하는 이메일 입니다";
    public static final String INVALID_USERNAME = "일치하는 계정이 없습니다";
    public static final String INVALID_PASSWORD = "비밀번호 오류입니다";
}
