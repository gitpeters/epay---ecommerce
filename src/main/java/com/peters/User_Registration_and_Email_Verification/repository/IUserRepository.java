package com.peters.User_Registration_and_Email_Verification.repository;

import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByEmail(String email);
}
