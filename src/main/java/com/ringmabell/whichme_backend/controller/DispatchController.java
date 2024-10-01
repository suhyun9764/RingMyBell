package com.ringmabell.whichme_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ringmabell.whichme_backend.dto.DispatchJoinDto;
import com.ringmabell.whichme_backend.response.Response;
import com.ringmabell.whichme_backend.service.DispatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
public class DispatchController {
	private final DispatchService dispatchService;

	@PostMapping("/join")
	public ResponseEntity<Response> join(@RequestBody @Valid DispatchJoinDto dispatchJoinDto){
		return ResponseEntity.ok(dispatchService.joinDispatch(dispatchJoinDto));
	}
}
