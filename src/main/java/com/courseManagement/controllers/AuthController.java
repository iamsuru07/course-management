package com.courseManagement.controllers;

import com.courseManagement.dto.auth.UserAccountRequestDto;
import com.courseManagement.dto.auth.UserLoginRequestDto;
import com.courseManagement.dto.auth.UserLoginResponseDto;
import com.courseManagement.dto.auth.UserResetPasswordRequestDto;
import com.courseManagement.services.AuthService;
import com.courseManagement.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody @Valid UserAccountRequestDto userAccountRequestDto) {
        logger.debug("Account creation request for {}", userAccountRequestDto);
        authService.createAccount(userAccountRequestDto);
        logger.info("User account created successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User account created"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> loginUser(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        logger.debug("User login request for {}", userLoginRequestDto);
        UserLoginResponseDto userLoginResponseDto = authService.authenticate(userLoginRequestDto);
        logger.info("Auth successful for user with userId {}", userLoginResponseDto.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Authentication successful",
                        userLoginResponseDto)
                );
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody @Valid UserResetPasswordRequestDto userResetPasswordRequestDto) {
        logger.debug("User request for resetting password for email {}", userResetPasswordRequestDto.getEmail());
        String newPassword = userResetPasswordRequestDto.getNewPassword();
        String confirmPassword = userResetPasswordRequestDto.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            logger.error("User did not entered same new and confirm password");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password should be same");
        }

        authService.resetUserPassword(userResetPasswordRequestDto);
        logger.info("User password reset successful");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Password updated successfully"));
    }
}
