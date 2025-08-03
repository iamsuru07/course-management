package com.courseManagement.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAccountRequestDto {
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}
