package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Admin;
import com.springbank.Spring.Bank.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String saveAdmin(Admin admin) {
        Optional<Admin> existingAdmin = adminRepository.findByUsername(admin.getUsername());

        if (existingAdmin.isPresent()) {
            return "Error: Username already exists!"; // Prevent duplicate usernames
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Encrypt password
        adminRepository.save(admin);
        return "Admin Created Successfully!";
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
