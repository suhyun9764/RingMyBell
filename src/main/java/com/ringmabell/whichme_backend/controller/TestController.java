package com.ringmabell.whichme_backend.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ringmabell.whichme_backend.jwt.CustomUserDetails;


@Controller
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/user")
	public ResponseEntity<String> getUserData(@AuthenticationPrincipal CustomUserDetails userDetails) {
		String username = userDetails.getUsername();
		return ResponseEntity.ok().body(username);
	}


}
