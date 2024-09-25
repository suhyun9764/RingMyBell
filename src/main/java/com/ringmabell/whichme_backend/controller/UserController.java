package com.ringmabell.whichme_backend.controller;

import com.ringmabell.whichme_backend.dto.EmailValidateDto;
import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.dto.UsernameValidateDto;
import com.ringmabell.whichme_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinDto joinDto) {
        userService.saveUser(joinDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate/username")
    public ResponseEntity<Boolean> validateUsername(@RequestParam @Valid  UsernameValidateDto usernameValidateDto ){
        return ResponseEntity.ok(userService.isAvailableUsername(usernameValidateDto));
    }

    @GetMapping("/validate/email")
    public ResponseEntity<Boolean> validateUsername(@RequestParam @Valid EmailValidateDto emailValidateDto){
        return ResponseEntity.ok(userService.isAvailableEmail(emailValidateDto));
    }


}
