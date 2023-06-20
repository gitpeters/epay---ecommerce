package com.peters.User_Registration_and_Email_Verification.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peters.User_Registration_and_Email_Verification.config.RestTemplateService;
import com.peters.User_Registration_and_Email_Verification.payment.dto.PaymentResponse;
import com.peters.User_Registration_and_Email_Verification.payment.dto.PaymentStatus;
import com.peters.User_Registration_and_Email_Verification.payment.entity.Payment;
import com.peters.User_Registration_and_Email_Verification.payment.repository.IPaymentRepository;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductOrderRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        if(order.getStatus().equalsIgnoreCase("purchased")){
            return ResponseEntity.accepted().body(new CustomResponse(HttpStatus.ACCEPTED, "This order has already been purchased by you"));
        }
        //get user from product order
        UserEntity user = order.getUser();

        double amountToBePaid = order.getTotalAmount() * 100;

        request.put("email", user.getEmail());
        request.put("amount", String.valueOf(amountToBePaid));

        String url = baseUrl +"transaction/initialize";

        ResponseEntity<CustomResponse> response = restTemplate.post(url, request, this.headers());
        if(response.getStatusCode() == HttpStatus.OK){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapResponse = objectMapper.convertValue(response.getBody().getData(), Map.class);

            Payment payment = Payment.builder()
                    .order(order)
                    .status(PaymentStatus.PENDING.name())
                    .paymentReference((String) mapResponse.get("reference"))
                    .paymentAccessCode((String) mapResponse.get("access_code"))
                    .build();
            paymentRepository.save(payment);

            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .authorization_url((String) mapResponse.get("authorization_url")).build();
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), paymentResponse, "Authorization URL created"));
        }

        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Something went wrong"));
    }

    @Override
    public ResponseEntity<CustomResponse> verifyPayment(String orderReference) {
        Optional<ProductOrder> orderOpt = orderRepository.findByReference(orderReference);
        if(orderOpt.isEmpty()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No order for this reference found"));
        }
        ProductOrder order = orderOpt.get();
        if(order.getStatus().equalsIgnoreCase("purchased")){
            return ResponseEntity.accepted().body(new CustomResponse(HttpStatus.ACCEPTED, "This payment has already been verified"));
        }

        Optional<Payment> paymentOpt = paymentRepository.findByOrder(order);
        if(paymentOpt.isEmpty()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No payment associated with this order"));
        }

        Payment payment = paymentOpt.get();
        String url = baseUrl + "transaction/verify/"+payment.getPaymentReference();
        ResponseEntity<CustomResponse> response = restTemplate.get(url, this.headers());
        if(response.getStatusCode() == HttpStatus.OK){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapResponse = objectMapper.convertValue(response.getBody().getData(), Map.class);
            int amount = (Integer) mapResponse.get("amount");
            payment.setAmount((double) amount);
            payment.setPaymentChannel((String) mapResponse.get("channel"));
            payment.setStatus(PaymentStatus.PAID.name());
            payment.setTransactionDate((String) mapResponse.get("paid_at"));
            paymentRepository.save(payment);
            order.setStatus("Purchased");
            orderRepository.save(order);

            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .authorization_url("Payment Verified").build();
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), paymentResponse, "Payment verified successfully"));
        }
        payment.setStatus(PaymentStatus.FAILED.name());
        paymentRepository.save(payment);
        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Payment failed"));
    }


    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+SECRET_KEY);
        headers.set("Content-type", "application/json");

        return headers;
    }
}
