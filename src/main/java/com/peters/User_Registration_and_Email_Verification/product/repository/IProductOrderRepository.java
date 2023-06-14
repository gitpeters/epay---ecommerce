package com.peters.User_Registration_and_Email_Verification.product.repository;

import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
