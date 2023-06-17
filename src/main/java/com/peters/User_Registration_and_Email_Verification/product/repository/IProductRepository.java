package com.peters.User_Registration_and_Email_Verification.product.repository;

import com.peters.User_Registration_and_Email_Verification.product.entity.Product;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByPriceBetween(Double min, Double max);
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByCategoryNameContainsIgnoreCase(String categoryName);
    List<Product> findByNameContainsIgnoreCase(String name);
    List<Product> findByUsers(UserEntity user);
}
