package com.courseManagement.repositories;

import com.courseManagement.entities.UserAccountEntity;
import com.courseManagement.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdminRepo extends JpaRepository<UserAccountEntity, Integer> {
    List<UserAccountEntity> findAllByEmail(String email);

    List<UserAccountEntity> findAllByRole(RoleEnum role);

    List<UserAccountEntity> findAllByFirstName(String firstName);

    List<UserAccountEntity> findAllByLastName(String lastName);

    Optional<UserAccountEntity> findByEmail(String email);
}
