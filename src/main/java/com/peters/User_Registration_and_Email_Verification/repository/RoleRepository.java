package com.peters.User_Registration_and_Email_Verification.repository;

import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
}
