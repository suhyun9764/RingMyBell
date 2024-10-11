package com.ringmabell.whichme_backend.dto;

import static com.ringmabell.whichme_backend.constants.DispatchMessage.*;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;

import com.ringmabell.whichme_backend.entitiy.dispatch.VehicleType;
import com.ringmabell.whichme_backend.entitiy.user.Disease;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;

@Getter
public class DispatchJoinDto {
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Pattern(
		regexp = VEHICLE_REGEX,
		message = VEHICLE_POLICY_MESSAGE
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

	@Schema(description = "차량 종류: EMERGENCY (0) - 응급 차량, "
			+ "FIRE (1) - 소방차량, "
			+ "POLICE (2) - 경찰 차량, "
			+ "COURT (3) - 법정 차량, "
			+ "EVACUATION (4) - 후송차량, "
			+ "ETC (5) - 기타")
	private VehicleType vehicleType;
}
