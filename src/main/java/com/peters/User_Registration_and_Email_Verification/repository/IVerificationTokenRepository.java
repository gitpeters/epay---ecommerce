package com.peters.User_Registration_and_Email_Verification.repository;

import com.peters.User_Registration_and_Email_Verification.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}
