package com.courseManagement.services;

import com.courseManagement.dto.admin.UserAccountRequestDto;
import com.courseManagement.dto.admin.UserAccountResponseDto;
import com.courseManagement.entities.UserAccountEntity;
import com.courseManagement.enums.RoleEnum;
import com.courseManagement.repositories.IAdminRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final IAdminRepo adminRepo;
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final ModelMapper modelMapper;

    public List<UserAccountResponseDto> getUsersList(UserAccountRequestDto userAccountRequestDto) {
        logger.debug("Started fetching user accounts with filters: {}", userAccountRequestDto);

        List<UserAccountEntity> userAccounts;

        if (userAccountRequestDto.getEmail() != null) {
            userAccounts = adminRepo.findAllByEmail(userAccountRequestDto.getEmail());
        } else if (userAccountRequestDto.getRole() != null) {
            RoleEnum roleEnum = RoleEnum.valueOf(userAccountRequestDto.getRole().toUpperCase());
            userAccounts = adminRepo.findAllByRole(roleEnum);
        } else if (userAccountRequestDto.getFirstName() != null) {
            userAccounts = adminRepo.findAllByFirstName(userAccountRequestDto.getFirstName());
        } else if (userAccountRequestDto.getLastName() != null) {
            userAccounts = adminRepo.findAllByLastName(userAccountRequestDto.getLastName());
        } else {
            userAccounts = adminRepo.findAll();
        }

        if (userAccounts.size() == 0) {
            logger.error("Data not found for given filter");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
        }

        return userAccounts
                .stream()
                .map(account -> modelMapper.map(account, UserAccountResponseDto.class))
                .toList();
    }

    public UserAccountResponseDto getUserById(int userId) {
        logger.debug("Started fetching user account for given userId: {}", userId);
        Optional<UserAccountEntity> userAccountEntity = adminRepo.findById(userId);

        if (userAccountEntity.isEmpty()) {
            logger.error("User account not found for given userId {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found");
        }
        return modelMapper.map(userAccountEntity.get(), UserAccountResponseDto.class);
    }

    public UserAccountResponseDto getUserByEmail(String email) {
        logger.debug("Started fetching user account for given email: {}", email);
        Optional<UserAccountEntity> userAccountEntity = adminRepo.findByEmail(email);

        if (userAccountEntity.isEmpty()) {
            logger.error("User account not found for given email {}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found");
        }
        return modelMapper.map(userAccountEntity.get(), UserAccountResponseDto.class);
    }

    public void deleteUserDetailsByUserId(int userId) {
        logger.debug("Started fetching user account existence for userId: {}", userId);
        Optional<UserAccountEntity> userAccountEntity = adminRepo.findById(userId);
        if (userAccountEntity.isEmpty()) {
            logger.error("User account does not exists");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account does not found");
        }

        logger.info("Account found for delete process {}", userId);

        adminRepo.deleteById(userId);
        logger.info("Account deleted");
    }
}
