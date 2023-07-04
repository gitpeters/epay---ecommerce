package com.peters.Epay.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "profile_picture")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imagePath;
    private String type;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
