package com.courseManagement.dto.auth;

import com.courseManagement.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponseDto {
    private int userId;
    private RoleEnum role;
    private String email;
    private String authToken;
}
