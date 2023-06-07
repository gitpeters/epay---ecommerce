package com.peters.User_Registration_and_Email_Verification.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] UN_SECURED_URL = {
            "/api/v1/register/**",
            "/api/v1/auth/**",
            "/api/v1/user-role/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] SECURED_URL = {
            "/api/v1/users/**",

    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(UN_SECURED_URL).permitAll()
                        .anyRequest().hasAnyAuthority("USER", "ADMIN")
                )
                .formLogin(formLogin -> {}
                )
                .csrf().disable();

        return http.build();
    }

}
