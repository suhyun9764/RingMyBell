package com.ringmabell.whichme_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UsernameValidateDto {

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;
}
