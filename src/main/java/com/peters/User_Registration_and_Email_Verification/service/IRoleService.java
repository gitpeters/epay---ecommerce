package com.peters.User_Registration_and_Email_Verification.service;

import com.peters.User_Registration_and_Email_Verification.dto.UserRoleRequestDto;
import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.entity.UserRole;

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
