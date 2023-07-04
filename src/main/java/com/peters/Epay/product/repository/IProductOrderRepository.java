package com.peters.Epay.product.repository;

import com.peters.Epay.product.entity.ProductOrder;
import com.peters.Epay.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Optional<ProductOrder> findByUser(UserEntity user);

    Optional<ProductOrder> findByReference(String orderReference);
}
