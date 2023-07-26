package com.peters.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationDto {
    private String email;
    private String firstName;
    private String token;
    private String url;
    private NotificationType type;
}
