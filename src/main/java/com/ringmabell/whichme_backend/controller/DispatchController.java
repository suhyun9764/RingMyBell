package com.ringmabell.whichme_backend.controller;

import static com.ringmabell.whichme_backend.constants.DispatchMessage.*;
import static com.ringmabell.whichme_backend.constants.DispatchSwaggerMessages.*;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ringmabell.whichme_backend.dto.DispatchJoinDto;
import com.ringmabell.whichme_backend.response.ErrorResponse;
import com.ringmabell.whichme_backend.response.Response;
import com.ringmabell.whichme_backend.service.DispatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
public class DispatchController {
	private final DispatchService dispatchService;


	@Operation(summary = "응급차량 회원가입 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "차량등록 완료",
			content = @Content(mediaType = "application/json",
				examples = {
					@ExampleObject(name = "차량등록 완료", value = COMPLETE_VEHICLE_JOIN_EXAMPLE),
				},
				schema = @Schema(implementation = Response.class)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "차량번호 중복",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(name = "차량번호 중복", value = VEHICLE_DUPLICATE_EXAMPLE),
				},
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "차량번호, 비밀번호 형식 오류 ",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(name = "차량번호 오류", value = VEHICLE_ERROR_EXAMPLE),
					@ExampleObject(name = "비밀번호 오류", value = PASSWORD_ERROR_EXAMPLE),
				},
				schema = @Schema(implementation = ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 부서 선택",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(name = "부서 오류", value = SUBUNIT_ERROR_EXAMPLE),
				},
				schema = @Schema(implementation = ErrorResponse.class)
			)
		)
	})
	@PostMapping("/join")
	public ResponseEntity<Response> join(@RequestBody @Valid DispatchJoinDto dispatchJoinDto){
		return ResponseEntity.ok(dispatchService.joinDispatch(dispatchJoinDto));
	}
}
