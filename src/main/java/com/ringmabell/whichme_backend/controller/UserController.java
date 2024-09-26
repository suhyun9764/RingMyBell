package com.ringmabell.whichme_backend.controller;

import com.ringmabell.whichme_backend.dto.JoinDto;
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

    @GetMapping("/validate/username")   // 아이디 중복확인
    public ResponseEntity<Boolean> validateUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.isAvailableUsername(username));
    }

    @GetMapping("/validate/email")  // 이메일 중복확인
    public ResponseEntity<Boolean> validateEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.isAvailableEmail(email));
    }


}
