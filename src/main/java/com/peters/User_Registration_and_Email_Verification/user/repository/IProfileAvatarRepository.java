package com.peters.User_Registration_and_Email_Verification.user.repository;

import com.peters.User_Registration_and_Email_Verification.user.entity.ProfileAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfileAvatarRepository extends JpaRepository<ProfileAvatar, Long> {
}
