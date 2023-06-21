package com.peters.User_Registration_and_Email_Verification.user.service;

import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserRoleRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRoleService {
    List<UserRole> getAllRoles();

    ResponseEntity<CustomResponse> createRole(UserRoleRequestDto request);

    ResponseEntity<CustomResponse> deleteRole(Long roleId);

    ResponseEntity<CustomResponse> findByName(String name);

    UserRole findById(Long roleId);

    ResponseEntity<CustomResponse> removeUserFromRole(Long userId, Long roleId);

    ResponseEntity<CustomResponse> assignUserToRole(Long userId, Long roleId);

    ResponseEntity<CustomResponse> removeAllUserFromRole(Long roleId);
}
