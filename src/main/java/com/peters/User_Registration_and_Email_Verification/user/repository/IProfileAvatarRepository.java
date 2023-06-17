package com.peters.User_Registration_and_Email_Verification.user.repository;

import com.peters.User_Registration_and_Email_Verification.user.entity.ProfileAvatar;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileAvatarRepository extends JpaRepository<ProfileAvatar, Long> {
    Optional<ProfileAvatar> findByUser(UserEntity user);
}
