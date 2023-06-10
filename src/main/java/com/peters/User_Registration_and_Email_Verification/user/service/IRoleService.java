package com.peters.User_Registration_and_Email_Verification.user.service;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserRoleRequestDto;

import java.util.List;

public interface IRoleService {
    List<UserRole> getAllRoles();

    UserRole createRole(UserRoleRequestDto request);

    void deleteRole(Long roleId);

    UserRole findByName(String name);

    UserRole findById(Long roleId);

    UserEntity removeUserFromRole(Long userId, Long roleId);

    UserEntity assignUserToRole(Long userId, Long roleId);

    UserRole removeAllUserFromRole(Long roleId);
}
