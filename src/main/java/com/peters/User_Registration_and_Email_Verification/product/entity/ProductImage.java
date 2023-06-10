package com.peters.User_Registration_and_Email_Verification.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @NaturalId
    private String imagePath;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Timestamp createdOn;
    private Timestamp lastModifiedOn;
    @PrePersist
    public void prePersist() {
        createdOn = Timestamp.from(Calendar.getInstance().toInstant());
        lastModifiedOn = Timestamp.from(Calendar.getInstance().toInstant());
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedOn = Timestamp.from(Calendar.getInstance().toInstant());
    }

}
