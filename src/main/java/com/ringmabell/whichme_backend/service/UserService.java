package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.response.Response;

public interface UserService {
	Response saveUser(JoinDto joinDto);

	Response isAvailableUsername(String usernameValidateDto);

	Response isAvailableEmail(String emailValidateDto);
}
