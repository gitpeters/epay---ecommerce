package com.peters.User_Registration_and_Email_Verification.user.dto;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAddressRequest {
    private String phoneNumber;
    private String street;
    private String city;
    private String state;
    private String country;
}
