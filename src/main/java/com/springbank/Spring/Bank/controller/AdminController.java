package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Admin;
import com.springbank.Spring.Bank.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminService.findByUsername(admin.getUsername());

        // সরাসরি পাসওয়ার্ড যাচাই করা হচ্ছে
        if (existingAdmin.isPresent() && admin.getPassword().equals(existingAdmin.get().getPassword())) {
            return ResponseEntity.ok("Login Successful!");
        }

        return ResponseEntity.status(401).body("Invalid Username or Password");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        adminService.saveAdmin(admin); // পাসওয়ার্ড এনক্রিপ্ট না করে সরাসরি সেভ করা হবে
        return ResponseEntity.ok("Admin Created Successfully!");
    }
}
