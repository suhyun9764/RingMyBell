package com.ringmabell.whichme_backend.controller;

import com.ringmabell.whichme_backend.dto.RegisterDto;
import com.ringmabell.whichme_backend.entitiy.User;
import com.ringmabell.whichme_backend.service.UserService;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody RegisterDto registerDto) {
        try {
            userService.saveUser(registerDto);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
