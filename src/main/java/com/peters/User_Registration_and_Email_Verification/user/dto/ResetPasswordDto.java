package com.peters.User_Registration_and_Email_Verification.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    private String email;
    private String newPassword;
}
