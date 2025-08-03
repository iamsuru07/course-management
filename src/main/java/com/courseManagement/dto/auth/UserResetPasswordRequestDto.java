package com.courseManagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResetPasswordRequestDto {
    @Email
    @ToString.Include
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "old password is required")
    @Size(min = 6, message = "old password minimum length should be 6")
    @Size(max = 16, message = "old password maximum length should be 16")
    private String oldPassword;

    @NotBlank(message = "new password is required")
    @Size(min = 6, message = "new password minimum length should be 6")
    @Size(max = 16, message = "new password maximum length should be 16")
    private String newPassword;

    @NotBlank(message = "confirm password is required")
    @Size(min = 6, message = "confirm password minimum length should be 6")
    @Size(max = 16, message = "confirm password maximum length should be 16")
    private String confirmPassword;
}
