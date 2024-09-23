package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.RegisterDto;
import com.ringmabell.whichme_backend.entitiy.User;
import com.ringmabell.whichme_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void saveUser(RegisterDto registerDto) {
        User userData = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .realName(registerDto.getRealName())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .address(registerDto.getAddress())
                .provider("LOCAL")
                .providerId(null)
                .build();

        userRepository.save(userData);


    }
}
