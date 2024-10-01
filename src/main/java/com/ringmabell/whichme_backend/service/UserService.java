package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.UserJoinDto;
import com.ringmabell.whichme_backend.response.Response;

public interface UserService {
	Response saveUser(UserJoinDto userJoinDto);

	Response isAvailableUsername(String usernameValidateDto);

	Response isAvailableEmail(String emailValidateDto);
}
