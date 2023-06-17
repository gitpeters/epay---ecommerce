package com.peters.User_Registration_and_Email_Verification.config;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.repository.IUserRepository;
import com.peters.User_Registration_and_Email_Verification.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
