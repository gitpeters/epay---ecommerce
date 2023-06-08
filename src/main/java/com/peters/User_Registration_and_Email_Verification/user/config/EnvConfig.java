package com.peters.User_Registration_and_Email_Verification.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:.env")
@Data
public class EnvConfig {
    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${MAIL_PASSWORD}")
    private String mailPassword;
}
