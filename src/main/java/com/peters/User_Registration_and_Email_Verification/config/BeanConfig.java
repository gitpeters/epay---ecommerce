package com.peters.User_Registration_and_Email_Verification.config;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.repository.IUserRepository;
import com.peters.User_Registration_and_Email_Verification.user.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Collections;



@Configuration

public class BeanConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner createDefaultUser(PlatformTransactionManager transactionManager) {
        return args -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

            transactionTemplate.execute(status -> {
                if (userRepository.findUserByEmail("admin@techiebros.com").isEmpty()) {
                    UserRole role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

                    UserEntity newUser = UserEntity.builder()
                            .email("admin@techiebros.com")
                            .firstName("Abraham")
                            .lastName("Peter")
                            .isEnabled(true)
                            .roles(Collections.singleton(role))
                            .password(passwordEncoder().encode("admin"))
                            .build();

                    entityManager.persist(role); // Save the UserRole entity
                    userRepository.save(newUser); // Save the UserEntity entity
                }
                return null;
            });
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate getRestTemplate() throws Exception {
        return new RestTemplate();
    }

}
