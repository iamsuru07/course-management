package com.courseManagement.dto.auth;

import com.courseManagement.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserAccountRequestDto {
    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @Email
    @NotBlank(message = "email is required")
    private String email;

    private RoleEnum role = RoleEnum.USER;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password minimum length should be 6")
    @Size(max = 16, message = "password maximum length should be 16")
    @ToString.Exclude
    private String password;
}
