package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.EmailValidateDto;
import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.dto.UsernameValidateDto;

public interface UserService {
    void saveUser(JoinDto joinDto);

    Boolean isAvailableUsername(UsernameValidateDto usernameValidateDto);

    Boolean isAvailableEmail(EmailValidateDto emailValidateDto);
}
