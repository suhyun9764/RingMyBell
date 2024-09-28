package com.ringmabell.whichme_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ringmabell.whichme_backend.dto.JoinDto;
import com.ringmabell.whichme_backend.response.Response;
import com.ringmabell.whichme_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;

	@PostMapping("/join")
	public ResponseEntity<Response> join(@RequestBody @Valid JoinDto joinDto) {
		return ResponseEntity.ok().body(userService.saveUser(joinDto));
	}

	@GetMapping("/validate/username")   // 아이디 중복확인
	public ResponseEntity<Response> validateUsername(@RequestParam("username") String username) {
		return ResponseEntity.ok().body(userService.isAvailableUsername(username));
	}

	@GetMapping("/validate/email")  // 이메일 중복확인
	public ResponseEntity<Response> validateEmail(@RequestParam("email") String email) {
		return ResponseEntity.ok().body(userService.isAvailableEmail(email));
	}

}
