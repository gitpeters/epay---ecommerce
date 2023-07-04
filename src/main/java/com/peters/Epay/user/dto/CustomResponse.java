package com.peters.Epay.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private String status;
    private Object data;
    private String message;

    public CustomResponse(HttpStatus status, String message) {
        this.status = (status.is2xxSuccessful() ? "success" : "error");
        this.message = message;
    }
}
