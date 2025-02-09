package com.springbank.Spring.Bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // এই এনোটেশনটি দিয়ে Spring এটিকে কনফিগারেশন ক্লাস হিসেবে চিহ্নিত করবে
public class SecurityConfig {

    // এই মেথডটি Spring Security কনফিগারেশন তৈরি করে
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/login").permitAll()
                        .requestMatchers("/api/customer/create").permitAll()
                        .requestMatchers("/api/customer/balance/**").permitAll()

                        .requestMatchers("/api/transaction/deposit").permitAll()//authenticated() // অথেন্টিকেশন লাগবে
                        .requestMatchers("/api/transaction/withdraw").permitAll()//authenticated()
                        .requestMatchers("/api/transaction/transfer").permitAll()//authenticated()
                        .requestMatchers("/api/transaction/history/**").permitAll()//authenticated()

                        .requestMatchers("/api/admin/**").authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build(); // নিরাপত্তা কনফিগারেশন তৈরি করে ফেরত দেওয়া হচ্ছে
    }

    // পাসওয়ার্ড এনকোড করার জন্য BCrypt পাসওয়ার্ড এনকোডার তৈরি
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt পাসওয়ার্ড এনকোডার ব্যবহার করা হচ্ছে
    }
}
