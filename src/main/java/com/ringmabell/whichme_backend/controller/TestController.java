package com.ringmabell.whichme_backend.controller;

import com.ringmabell.whichme_backend.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
@Tag(name = "TestController", description = "test authorization, certification etc")
public class TestController {

    @GetMapping("/user")
    public ResponseEntity<String> getUserData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok().body(username);
    }


}
