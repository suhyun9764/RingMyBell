package com.ringmabell.whichme_backend.constants;

public class RegistrationPolicy {
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 20;

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 16;

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String VEHICLE_REGEX = "^(998|999)[가-힣]{1}(?<![ㄱ-ㅎㅏ-ㅣ])[ㄱ-ㅎㅏ-ㅣ]{0}\\d{4}$";
    public static final String BIRTHDAY_REGEX = "\\d{8}";
}
