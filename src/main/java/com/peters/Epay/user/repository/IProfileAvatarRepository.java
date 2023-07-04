package com.peters.Epay.user.repository;

import com.peters.Epay.user.entity.ProfileAvatar;
import com.peters.Epay.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileAvatarRepository extends JpaRepository<ProfileAvatar, Long> {
    Optional<ProfileAvatar> findByUser(UserEntity user);
}
