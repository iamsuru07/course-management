package com.courseManagement.services;

import com.courseManagement.dto.auth.UserAccountRequestDto;
import com.courseManagement.dto.auth.UserLoginRequestDto;
import com.courseManagement.dto.auth.UserLoginResponseDto;
import com.courseManagement.dto.auth.UserResetPasswordRequestDto;
import com.courseManagement.entities.UserAccountEntity;
import com.courseManagement.repositories.IUserAccountRepo;
import com.courseManagement.utils.AuthTokenGenerator;
import com.courseManagement.utils.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordHasher passwordHasher;
    private final IUserAccountRepo userAccountRepo;
    private final AuthTokenGenerator authTokenGenerator;

    private final ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public void createAccount(UserAccountRequestDto userAccountRequestDto) {
        String email = userAccountRequestDto.getEmail();

        Optional<UserAccountEntity> isAccountExists = userAccountRepo.findByEmail(email);

        if (isAccountExists.isPresent()) {
            logger.error("User account already exists with email {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User account already exists");
        }

        String hashPassword = passwordHasher.hashPassword(userAccountRequestDto.getPassword());

        UserAccountEntity userAccountEntity = modelMapper.map(userAccountRequestDto, UserAccountEntity.class);
        userAccountEntity.setPassword(hashPassword);

        userAccountRepo.save(userAccountEntity);
    }

    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {
        Optional<UserAccountEntity> userAccountEntity = userAccountRepo.findByEmail(userLoginRequestDto.getEmail());

        if (!userAccountEntity.isPresent()) {
            logger.error("User account does not exists with given email {}", userLoginRequestDto.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authentication failed");
        }

        UserAccountEntity authUser = userAccountEntity.get();

        boolean hasCorrectPassword = passwordHasher.
                matchPassword(
                        userLoginRequestDto.getPassword(),
                        authUser.getPassword()
                );

        if (!hasCorrectPassword) {
            logger.warn("User entered incorrect credentials {}", userLoginRequestDto.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication failed");
        }

        return new UserLoginResponseDto(
                authUser.getUserId(),
                authUser.getRole(),
                authUser.getEmail(),
                authTokenGenerator.generateToken(
                        authUser.getUserId(),
                        authUser.getEmail()
                )
        );
    }

    public void resetUserPassword(UserResetPasswordRequestDto userResetPasswordRequestDto) {
        Optional<UserAccountEntity> isAccountExists = userAccountRepo.findByEmail(userResetPasswordRequestDto.getEmail());

        if (!isAccountExists.isPresent()) {
            logger.error("User account does not exists with given email, failed to reset password {}", userResetPasswordRequestDto.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account does not exists");
        }

        UserAccountEntity authUser = isAccountExists.get();

        boolean hasCorrectPassword = passwordHasher.
                matchPassword(
                        userResetPasswordRequestDto.getOldPassword(),
                        authUser.getPassword()
                );

        if (!hasCorrectPassword) {
            logger.warn("Old password is incorrect for given email {}", userResetPasswordRequestDto.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }

        if (userResetPasswordRequestDto.getOldPassword().equals(userResetPasswordRequestDto.getConfirmPassword())) {
            logger.error("Old and new password can not be same {}", userResetPasswordRequestDto.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Old and new password can not be same");
        }

        authUser.setPassword(passwordHasher.hashPassword(userResetPasswordRequestDto.getConfirmPassword()));
        userAccountRepo.save(authUser);
        logger.info("Password reset successfully for given {}", userResetPasswordRequestDto.getEmail());
    }
}