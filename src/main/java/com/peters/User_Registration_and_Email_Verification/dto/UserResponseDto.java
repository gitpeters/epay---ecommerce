package com.peters.User_Registration_and_Email_Verification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.peters.User_Registration_and_Email_Verification.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserRole> role;
    private boolean isEnabled;
}
