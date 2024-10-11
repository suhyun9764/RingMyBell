package com.ringmabell.whichme_backend.dto;

import static com.ringmabell.whichme_backend.constants.DispatchMessage.*;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;

import com.ringmabell.whichme_backend.entitiy.dispatch.VehicleType;
import com.ringmabell.whichme_backend.entitiy.user.Disease;
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
	private VehicleType vehicleType;
}
