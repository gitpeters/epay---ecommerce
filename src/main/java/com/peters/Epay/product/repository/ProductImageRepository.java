package com.peters.Epay.product.repository;

import com.peters.Epay.product.entity.Product;
import com.peters.Epay.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByName(String name);
     List<ProductImage> findByProduct(Product product);

    @Query("SELECT pi.imagePath FROM ProductImage pi WHERE pi.product = :product")
    List<String> findByImagePath(Product product);
}
