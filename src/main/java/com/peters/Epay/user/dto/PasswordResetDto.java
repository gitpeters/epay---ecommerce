package com.peters.Epay.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {
    private String email;
    private String firstName;
    private int token;
    private NotificationType type;
}
