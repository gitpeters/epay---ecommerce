package com.peters.Epay.user.repository;

import com.peters.Epay.user.entity.UserEntity;
import com.peters.Epay.user.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(UserEntity existingUser);
}
