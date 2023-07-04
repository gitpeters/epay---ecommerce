package com.peters.Epay.payment.repository;

import com.peters.Epay.payment.entity.Payment;
import com.peters.Epay.product.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(ProductOrder order);
}
