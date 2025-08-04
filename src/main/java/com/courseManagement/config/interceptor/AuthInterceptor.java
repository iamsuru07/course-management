package com.courseManagement.config.interceptor;

import com.courseManagement.entities.UserAccountEntity;
import com.courseManagement.enums.RoleEnum;
import com.courseManagement.repositories.IAdminRepo;
import com.courseManagement.utils.AuthTokenGenerator;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final IAdminRepo adminRepo;
    private final AuthTokenGenerator authTokenGenerator;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getRequestURI();

        // Auth anyone can use
        if (path.contains("/api/v1/auth")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.split(" ")[1];

        int userId;

        try {
            userId = authTokenGenerator.verifyAuthToken(token);
            if (userId == -1) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }

        // Fetch user
        Optional<UserAccountEntity> userAccountEntity = adminRepo.findById(userId);
        if (userAccountEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }

        UserAccountEntity currentUser = userAccountEntity.get();

        // Admin only admin can use
        if (path.contains("/api/v1/admin") && currentUser.getRole() != RoleEnum.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Admins only");
        }

        // Validating course path if role is admin than every path can use or role is user than only GET

        if (path.contains("/api/v1/course")) {
            if (!request.getMethod().equals("GET") && !currentUser.getRole().equals(RoleEnum.ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Admins only");
            }
        }

        request.setAttribute("authenticatedUser", currentUser);

        return true;
    }
}

