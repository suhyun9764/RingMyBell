package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.JoinDto;

public interface UserService {
    void saveUser(JoinDto joinDto);

    Boolean isAvailableUsername(String usernameValidateDto);

    Boolean isAvailableEmail(String emailValidateDto);
}
