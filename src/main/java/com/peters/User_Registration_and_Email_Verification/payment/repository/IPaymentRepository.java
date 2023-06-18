package com.peters.User_Registration_and_Email_Verification.payment.repository;

import com.peters.User_Registration_and_Email_Verification.payment.entity.Payment;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(ProductOrder order);
}
