package com.peters.Epay.payment.service;

import com.peters.Epay.user.dto.CustomResponse;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<CustomResponse> initiatePayment(String orderReference);

    ResponseEntity<CustomResponse> verifyPayment(String orderReference);
}
