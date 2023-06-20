package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.payment.service.IPaymentService;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@Tag(name = "payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("/initiate-payment")
    public ResponseEntity<CustomResponse> initiatePayment(@RequestParam(name = "orderReference") String orderReference){
        return paymentService.initiatePayment(orderReference);
    }

    @GetMapping("/verify-payment")
    public ResponseEntity<CustomResponse> verifyPayment(@RequestParam(name = "orderReference") String orderReference){
        return paymentService.verifyPayment(orderReference);
    }
}
