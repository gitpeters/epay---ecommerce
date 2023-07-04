package com.peters.Epay.user.service;

import com.peters.Epay.user.dto.CustomResponse;
import com.peters.Epay.user.entity.UserRole;
import com.peters.Epay.user.dto.UserRoleRequestDto;
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
