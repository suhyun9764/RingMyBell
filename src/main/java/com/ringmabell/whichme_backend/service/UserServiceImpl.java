package com.ringmabell.whichme_backend.service;

import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.*;
import static com.ringmabell.whichme_backend.constants.UserMessages.*;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ringmabell.whichme_backend.dto.UserJoinDto;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.user.User;
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
	public Response saveUser(UserJoinDto userJoinDto) {
		validateUserDetails(userJoinDto);
		User user = buildUserFromDto(userJoinDto);
		userRepository.save(user);
		return successResponse(COMPLETE_JOIN);
	}

	private User buildUserFromDto(UserJoinDto userJoinDto) {
		return User.builder()
			.loginId(userJoinDto.getUsername())
			.password(passwordEncoder.encode(userJoinDto.getPassword()))
			.realName(userJoinDto.getRealName())
			.email(userJoinDto.getEmail())
			.phone(userJoinDto.getPhone())
			.address(userJoinDto.getAddress())
			.role(Role.ROLE_USER)
			.provider("LOCAL")
			.providerId(null)
			.build();
	}

	private void validateUserDetails(UserJoinDto userJoinDto) {
		checkIfUsernameExists(userJoinDto.getUsername());
		checkIfEmailExists(userJoinDto.getEmail());
	}

	private void checkIfUsernameExists(String username) {
		if (userRepository.existsByLoginId(username)) {
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
		if (userRepository.existsByLoginId(username)) {
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
