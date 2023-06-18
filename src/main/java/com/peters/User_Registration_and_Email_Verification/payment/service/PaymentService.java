package com.peters.User_Registration_and_Email_Verification.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peters.User_Registration_and_Email_Verification.config.RestTemplateService;
import com.peters.User_Registration_and_Email_Verification.payment.dto.PaymentStatus;
import com.peters.User_Registration_and_Email_Verification.payment.entity.Payment;
import com.peters.User_Registration_and_Email_Verification.payment.repository.IPaymentRepository;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductOrderRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    private final IProductOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final RestTemplateService restTemplate;
    @Value("${paystack.secret-key}")
    private String SECRET_KEY;
    @Value("${paystack.base-url}")
    private String baseUrl;
    @Override
    public ResponseEntity<CustomResponse> initiatePayment(String orderReference) {
        Map<String, String> request = new HashMap<>();
        if(orderReference == null || orderReference.isEmpty()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "orderReference is required"));
        }
        Optional<ProductOrder> orderOpt = orderRepository.findByReference(orderReference);
        if(orderOpt.isEmpty()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No order is found for this reference"));
        }

        ProductOrder order = orderOpt.get();
        //get user from product order
        UserEntity user = order.getUser();

        request.put("email", user.getEmail());
        request.put("amount", String.valueOf(order.getTotalAmount()));

        String url = baseUrl +"transaction/initialize";

        ResponseEntity<CustomResponse> response = restTemplate.post(url, request, this.headers());
        if(response.getStatusCode() == HttpStatus.OK){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapResponse = objectMapper.convertValue(response.getBody().getData(), Map.class);

            Payment payment = Payment.builder()
                    .order(order)
                    .status(PaymentStatus.PENDING.name())
                    .amount(order.getTotalAmount())
                    .paymentReference((String) mapResponse.get("reference"))
                    .paymentAccessCode((String) mapResponse.get("access_code"))
                    .build();
            paymentRepository.save(payment);
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, (String) mapResponse.get("authorization_url")));
        }

        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Something went wrong"));
    }

//    @Async
//    private ResponseEntity<CustomResponse> processPayment(Map<String, String> request, ProductOrder order) {
//
//        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Something went wrong!"));
//    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+SECRET_KEY);
        headers.set("Content-type", "application/json");

        return headers;
    }
}
