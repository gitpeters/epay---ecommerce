package com.peters.User_Registration_and_Email_Verification.user.repository;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findByUser(Long userId);
}
