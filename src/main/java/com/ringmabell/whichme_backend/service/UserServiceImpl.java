package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.EmailValidateDto;
import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.dto.UsernameValidateDto;
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
    public void saveUser(JoinDto joinDto) {
        User userData = User.builder()
                .username(joinDto.getUsername())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .realName(joinDto.getRealName())
                .email(joinDto.getEmail())
                .phone(joinDto.getPhone())
                .address(joinDto.getAddress())
                .provider("LOCAL")
                .providerId(null)
                .build();

        userRepository.save(userData);
    }

    @Override
    public Boolean isAvailableUsername(UsernameValidateDto usernameValidateDto) {
        if(userRepository.existsByUsername(usernameValidateDto.getUsername()))
            return false;

        return true;
    }

    @Override
    public Boolean isAvailableEmail(EmailValidateDto emailValidateDto) {
        if(userRepository.existsByEmail(emailValidateDto.getEmail()))
            return false;

        return null;
    }
}
