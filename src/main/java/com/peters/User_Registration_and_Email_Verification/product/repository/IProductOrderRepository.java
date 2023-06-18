package com.peters.User_Registration_and_Email_Verification.product.repository;

import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Optional<ProductOrder> findByUser(UserEntity user);

    Optional<ProductOrder> findByReference(String orderReference);
}
