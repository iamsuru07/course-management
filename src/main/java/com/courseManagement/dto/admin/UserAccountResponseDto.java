package com.courseManagement.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountResponseDto {
    private int userId;
    private String firstName;
    private String lastname;
    private String role;
    private String email;
    private Date createdAt;
}
