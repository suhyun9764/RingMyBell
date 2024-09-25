package com.ringmabell.whichme_backend.service;

import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.EMAIL_REGEX;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.USERNAME_MAX_LENGTH;
import static com.ringmabell.whichme_backend.constants.RegistrationPolicy.USERNAME_MIN_LENGTH;
import static com.ringmabell.whichme_backend.constants.UserMessages.ALREADY_EXIST_EMAIL;
import static com.ringmabell.whichme_backend.constants.UserMessages.ALREADY_EXIST_USERNAME;

import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.entitiy.User;
import com.ringmabell.whichme_backend.exception.DuplicateException;
import com.ringmabell.whichme_backend.repository.UserRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveUser(JoinDto joinDto) {
        validateJoinDto(joinDto);
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

    private void validateJoinDto(JoinDto joinDto) {
        if (userRepository.existsByUsername(joinDto.getUsername())) {
            throw new DuplicateException(ALREADY_EXIST_USERNAME);
        }

        if (userRepository.existsByEmail(joinDto.getEmail())) {
            throw new DuplicateException(ALREADY_EXIST_EMAIL);
        }
    }

    @Override
    public Boolean isAvailableUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean isAvailableEmail(String email) {
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        if (emailPattern.matcher(email).matches() == false) {
            return false;
        }

        if (userRepository.existsByEmail(email)) {
            return false;
        }

        return true;
    }
}
