package com.ringmabell.whichme_backend.controller;

import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.AVAILABLE_EMAIL_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.AVAILABLE_USERNAME_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.COMPLETE_USER_JOIN_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.EMAIL_ALREADY_EXISTS_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.EMAIL_DUPLICATE_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.EMAIL_FORMAT_ERROR_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.INVALID_EMAIL_FORMAT_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.PASSWORD_ERROR_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.USERNAME_ALREADY_EXISTS_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.USERNAME_DUPLICATE_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.USERNAME_ERROR_EXAMPLE;
import static com.ringmabell.whichme_backend.constants.UserSwaggerMessages.USERNAME_SIZE_CONSTRAINT_EXAMPLE;

import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.ringmabell.whichme_backend.dto.UserJoinDto;
import com.ringmabell.whichme_backend.response.ErrorResponse;
import com.ringmabell.whichme_backend.response.Response;
import com.ringmabell.whichme_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
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
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "일반유저 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입이 완료되었습니다",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "회원가입 완료", value = COMPLETE_USER_JOIN_EXAMPLE)
                            },
                            schema = @Schema(implementation = Response.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "아이디 중복",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "아이디 중복", value = USERNAME_DUPLICATE_EXAMPLE),
                                    @ExampleObject(name = "이메일 중복", value = EMAIL_DUPLICATE_EXAMPLE)
                            },
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "아이디, 비밀번호, 이메일 등 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "아이디 오류", value = USERNAME_ERROR_EXAMPLE),
                                    @ExampleObject(name = "비밀번호 오류", value = PASSWORD_ERROR_EXAMPLE),
                                    @ExampleObject(name = "이메일 오류", value = EMAIL_FORMAT_ERROR_EXAMPLE)
                            },
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Response> join(@RequestBody @Valid UserJoinDto userJoinDto) {
        return ResponseEntity.ok().body(userService.saveUser(userJoinDto));
    }


    @GetMapping("/validate/username")   // 아이디 중복확인
    @Operation(summary = "유저 id 사용가능 여부 확인 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "아이디 유효성 확인",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "사용가능", value = AVAILABLE_USERNAME_EXAMPLE),
                                    @ExampleObject(name = "사용불가(글자 수 오류)", value = USERNAME_SIZE_CONSTRAINT_EXAMPLE),
                                    @ExampleObject(name = "사용불가(중복)", value = USERNAME_ALREADY_EXISTS_EXAMPLE)
                            },
                            schema = @Schema(implementation = Response.class)
                    )
            )
    })
    public ResponseEntity<Response> validateUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok().body(userService.isAvailableUsername(username));
    }

    @GetMapping("/validate/email")  // 이메일 중복확인
    @Operation(summary = "유저 email 사용가능 여부 확인 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 유효성 확인",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "사용가능", value = AVAILABLE_EMAIL_EXAMPLE),
                                    @ExampleObject(name = "사용불가(이메일 형식 오류)", value = INVALID_EMAIL_FORMAT_EXAMPLE),
                                    @ExampleObject(name = "사용불가(중복)", value = EMAIL_ALREADY_EXISTS_EXAMPLE)
                            },
                            schema = @Schema(implementation = Response.class)
                    )
            )
    })
    public ResponseEntity<Response> validateEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok().body(userService.isAvailableEmail(email));
    }


    @GetMapping("/test")
    public void geoTest(@RequestParam double a, @RequestParam double b)
            throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new Builder()
                .apiKey("AIzaSyA64JJv3el8oIgGsFyVlDiAhK_0IDk3ZEk")
                .build();

        double latitude = 37.4979;
        double longitude = 127.0276;

        GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(a, b)).await();
        System.out.println(results[0].addressComponents[2]);
        System.out.println(results[0].formattedAddress);


    }
}
