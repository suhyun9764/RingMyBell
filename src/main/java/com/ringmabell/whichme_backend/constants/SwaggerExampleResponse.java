package com.ringmabell.whichme_backend.constants;

public class SwaggerExampleResponse {
	public static final String USERNAME_DUPLICATE_EXAMPLE = "{ \"success\": false,  \"httpStatus\": \"CONFLICT\", \"code\": 409, \"message\": \"아이디가 중복되었습니다.\" }";
	public static final String EMAIL_DUPLICATE_EXAMPLE = "{ \"success\": false,  \"httpStatus\": \"CONFLICT\",\"code\": 409, \"message\": \"이메일이 중복되었습니다.\" }";
	public static final String USERNAME_ERROR_EXAMPLE = "{ \"success\": false,\"httpStatus\": \"BAD_REQUEST\", \"code\": 400, \"message\": \"아이디는 3자 이상 20자 이하여야 합니다.\" }";
	public static final String PASSWORD_ERROR_EXAMPLE = "{ \"success\": false, \"httpStatus\": \"BAD_REQUEST\",\"code\": 400, \"message\": \"비밀번호는 영문, 숫자, 특수문자(@,$,!,%,*,?,&)를 포함해 8자 이상 16자 이하여야 합니다.\" }";
	public static final String EMAIL_FORMAT_ERROR_EXAMPLE = "{ \"success\": false,\"httpStatus\": \"BAD_REQUEST\", \"code\": 400, \"message\": \"올바른 이메일 형식이 아닙니다.\" }";
	public static final String AVAILABLE_USERNAME_EXAMPLE = "{ \"success\": true, \"message\": \"사용 가능한 아이디입니다\" }";
	public static final String USERNAME_SIZE_CONSTRAINT_EXAMPLE = "{ \"success\": false, \"message\": \"아이디는 3자 이상 20자 이하여야 합니다\" }";
	public static final String USERNAME_ALREADY_EXISTS_EXAMPLE = "{ \"success\": false, \"message\": \"이미 존재하는 아이디입니다\" }";
	public static final String AVAILABLE_EMAIL_EXAMPLE = "{ \"success\": true, \"message\": \"사용 가능한 이메일입니다\" }";
	public static final String EMAIL_ALREADY_EXISTS_EXAMPLE = "{ \"success\": false, \"message\": \"이미 존재하는 이메일입니다\" }";
	public static final String INVALID_EMAIL_FORMAT_EXAMPLE = "{ \"success\": false, \"message\": \"올바른 이메일 형식이 아닙니다\" }";
}



