package com.peters.User_Registration_and_Email_Verification.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "category")
    private Collection<Product> products;

    public void removeAllProductsFromCategory(){
        if(this.getProducts() != null){
            List<Product> productCategory = this.getProducts().stream().toList();
            productCategory.forEach(this::removeProductFromCategory);
        }
    }

    // remove single user from role
    public void removeProductFromCategory(Product product) {
        product.getCategory().remove(this);
        this.getProducts().remove(product);
    }

    // assign role to user
    public void assignProductToCategory(Product product){
        product.getCategory().add(this);
        this.getProducts().add(product);
    }
}
