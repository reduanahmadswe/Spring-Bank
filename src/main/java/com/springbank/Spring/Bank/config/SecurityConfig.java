package com.springbank.Spring.Bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/admin/register",
                                "/api/admin/login",
                                "/api/customer/create",
                                "/api/customer/login",
                                "/api/customer/balance",
                                "/accounts/balance",
                                "/accounts/customerdata",
                                "/api/transaction/deposit",
                                "/api/transaction/withdraw",
                                "/api/transaction/transfer",
                                "/api/transaction/history/**",
                                "/api/accounts/close/**",
                                "/api/accounts/reopen/**")
                        .permitAll() // Public routes
//                        .requestMatchers(
//                                "/api/customer/balance/**",
//                                "/api/transaction/transfer",
//                                "/api/transaction/history/**")
//                        .authenticated() // Authenticated routes
                        .anyRequest().authenticated() // All other requests need authentication
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCrypt for password encryption
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}
