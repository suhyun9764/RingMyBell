package com.ringmabell.whichme_backend.dto;

import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.BIRTHDAY_REGEX;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.EMAIL_REGEX;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.PASSWORD_MAX_LENGTH;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.PASSWORD_MIN_LENGTH;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.PASSWORD_REGEX;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.USERNAME_MAX_LENGTH;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.USERNAME_MIN_LENGTH;
import static com.ringmabell.whichme_backend.constants.UserMessages.BIRTHDAY_TYPE_ERROR;
import static com.ringmabell.whichme_backend.constants.UserMessages.EMAIL_POLICY_MESSAGE;
import static com.ringmabell.whichme_backend.constants.UserMessages.NOT_BLANK_MESSAGE;
import static com.ringmabell.whichme_backend.constants.UserMessages.PASSWORD_POLICY_MESSAGE;
import static com.ringmabell.whichme_backend.constants.UserMessages.USERNAME_POLICY_MESSAGE;

import com.ringmabell.whichme_backend.entitiy.user.Disease;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;

@Getter
public class UserJoinDto {

    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = USERNAME_POLICY_MESSAGE)
    private String username;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_POLICY_MESSAGE)
    @Pattern(
            regexp = PASSWORD_REGEX,
            message = PASSWORD_POLICY_MESSAGE
    )
    private String password;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String realName;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(
            regexp = EMAIL_REGEX,
            message = EMAIL_POLICY_MESSAGE
    )
    @Schema(example = "test@naver.com")
    private String email;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String phone;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String address;
    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = BIRTHDAY_REGEX, message = BIRTHDAY_TYPE_ERROR)
    private String birthday;  // 8자리 생년월일
    @Schema(description = "질병 목록 (복수 선택 가능). 질병 설명: "
            + "0: 심장병, "
            + "1: 빈혈, "
            + "2: 이석증, "
            + "3: 눈 관련 질환, "
            + "4: 고혈압, "
            + "5: 저혈압")
    private Set<Disease> diseases;
}
