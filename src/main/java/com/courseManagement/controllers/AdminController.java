package com.courseManagement.controllers;

import com.courseManagement.dto.admin.UserAccountRequestDto;
import com.courseManagement.dto.admin.UserAccountResponseDto;
import com.courseManagement.services.AdminService;
import com.courseManagement.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user-account")
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAccountResponseDto>>> fetchUsers(@ModelAttribute UserAccountRequestDto userAccountRequestDto) {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "User account fetched successfully",
                        adminService.getUsersList(userAccountRequestDto)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserAccountResponseDto>> fetchUserById(@PathVariable("userId") int userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "User account fetched successfully",
                        adminService.getUserById(userId)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUserByUserId(@PathVariable("userId") int userId) {
        adminService.deleteUserDetailsByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User account deleted successfully"));
    }
}
