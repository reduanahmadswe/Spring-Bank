package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Admin;
import com.springbank.Spring.Bank.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public void saveAdmin(Admin admin) {
        adminRepository.save(admin); // পাসওয়ার্ড এনক্রিপ্ট না করে সরাসরি সেভ করা হবে
    }
}
