package com.peters.User_Registration_and_Email_Verification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users = new HashSet<>();

    public UserRole(String name) {
        this.name = name;
    }

    // remove all users from role
    public void removeAllUsersFromRole(){
        if(this.getUsers() != null){
            List<UserEntity> usersInRole = this.getUsers().stream().toList();
            usersInRole.forEach(this::removeUserFromRole);
        }
    }

    // remove single user from role
    public void removeUserFromRole(UserEntity user) {
        user.getRole().remove(this);
        this.getUsers().remove(user);
    }

    // assign role to user
    public void assignUserToRole(UserEntity user){
        user.getRole().add(this);
        this.getUsers().add(user);
    }


}
