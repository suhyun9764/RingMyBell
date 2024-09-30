package com.ringmabell.whichme_backend.service;

import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.User;
import com.ringmabell.whichme_backend.exception.exptions.DuplicateException;
import com.ringmabell.whichme_backend.repository.UserRepository;
import com.ringmabell.whichme_backend.response.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Response saveUser(JoinDto joinDto) {
		validateUserDetails(joinDto);
		User user = buildUserFromDto(joinDto);
		userRepository.save(user);
		return successResponse(COMPLETE_JOIN);
	}

	private User buildUserFromDto(JoinDto joinDto) {
		return User.builder()
			.username(joinDto.getUsername())
			.password(passwordEncoder.encode(joinDto.getPassword()))
			.realName(joinDto.getRealName())
			.email(joinDto.getEmail())
			.phone(joinDto.getPhone())
			.address(joinDto.getAddress())
			.role(Role.ROLE_USER)
			.provider("LOCAL")
			.providerId(null)
			.build();
	}

	private void validateUserDetails(JoinDto joinDto) {
		checkIfUsernameExists(joinDto.getUsername());
		checkIfEmailExists(joinDto.getEmail());
	}

	private void checkIfUsernameExists(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new DuplicateException(ALREADY_EXIST_USERNAME);
		}
	}

	private void checkIfEmailExists(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateException(ALREADY_EXIST_EMAIL);
		}
	}

	@Override
	public Response isAvailableUsername(String username) {
		if (isInvalidUsernameLength(username)) {
			return failureResponse(USERNAME_POLICY_MESSAGE);
		}
		if (userRepository.existsByUsername(username)) {
			return failureResponse(ALREADY_EXIST_USERNAME);
		}
		return successResponse(AVAILABLE_USERNAME);
	}

	private boolean isInvalidUsernameLength(String username) {
		return username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH;
	}

	@Override
	public Response isAvailableEmail(String email) {
		if (!isValidEmailFormat(email)) {
			return failureResponse(EMAIL_POLICY_MESSAGE);
		}
		if (userRepository.existsByEmail(email)) {
			return failureResponse(ALREADY_EXIST_EMAIL);
		}
		return successResponse(AVAILABLE_EMAIL);
	}

	private boolean isValidEmailFormat(String email) {
		Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
		return emailPattern.matcher(email).matches();
	}

	private Response successResponse(String message) {
		return Response.builder()
			.success(true)
			.message(message)
			.build();
	}

	private Response failureResponse(String message) {
		return Response.builder()
			.success(false)
			.message(message)
			.build();
	}
}
