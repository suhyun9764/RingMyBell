package com.ringmabell.whichme_backend.dto;

import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DispatchJoinDto {
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Pattern(
		regexp = "^(998|999)[가-힣]{1}(?<![ㄱ-ㅎㅏ-ㅣ])[ㄱ-ㅎㅏ-ㅣ]{0}\\d{4}$",
		message = "차량 번호는 998 또는 999로 시작하고, 받침이 없는 한글 한 글자, 마지막 4자리가 숫자로 구성되어야 합니다."
	)
	private String vehicleNumber;
	@NotNull(message = NOT_BLANK_MESSAGE)
	private Long subUnit;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_POLICY_MESSAGE)
	@Pattern(
		regexp = PASSWORD_REGEX,
		message = PASSWORD_POLICY_MESSAGE
	)
	private String password;
}
