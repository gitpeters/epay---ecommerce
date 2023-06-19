package com.peters.User_Registration_and_Email_Verification.payment.service;

import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<CustomResponse> initiatePayment(String orderReference);

    ResponseEntity<CustomResponse> verifyPayment(String orderReference);
}
