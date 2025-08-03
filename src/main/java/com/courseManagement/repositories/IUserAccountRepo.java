package com.courseManagement.repositories;

import com.courseManagement.entities.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserAccountRepo extends JpaRepository<UserAccountEntity, Integer> {
    Optional<UserAccountEntity> findByEmail(String email);
}