package com.peters.User_Registration_and_Email_Verification.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private static final String[] UN_SECURED_URL = {
            "/api/v1/user/**",
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

    private static final String[] ADMIN_SECURED_URL = {
            "/api/v1/user-role/**",
            "/api/v1/admin/**"

    };

    private static final String[] CUSTOMER_SECURED_URL = {
            "/api/v1/customer/**",
            "/api/v1/payment/**"

    };

    private static final String[] VENDOR_SECURED_URL = {
            "/api/v1/product/**"

    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(UN_SECURED_URL).permitAll()
                        .requestMatchers(ADMIN_SECURED_URL).hasRole("ADMIN")
                        .requestMatchers(VENDOR_SECURED_URL).hasRole("VENDOR")
                        .requestMatchers(CUSTOMER_SECURED_URL).hasRole("USER")
                        .anyRequest().hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                )
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
