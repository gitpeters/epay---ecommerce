package com.peters.Epay.user.repository;

import com.peters.Epay.user.entity.UserEntity;
import com.peters.Epay.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);

    List<UserRole> findByUsers(UserEntity user);
}
