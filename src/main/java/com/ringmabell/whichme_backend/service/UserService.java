package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.RegisterDto;

public interface UserService {
    void saveUser(RegisterDto registerDto);
}
