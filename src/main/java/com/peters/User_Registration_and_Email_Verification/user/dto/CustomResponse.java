package com.peters.User_Registration_and_Email_Verification.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private HttpStatus status;
    private Object data;
    private String message;

    public CustomResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
