package com.peters.Epay.product.repository;

import com.peters.Epay.product.entity.Product;
import com.peters.Epay.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByName(String name);
    List<ProductCategory> findByProducts(Product product);
}
