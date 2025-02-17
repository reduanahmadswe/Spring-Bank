package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Admin;
import com.springbank.Spring.Bank.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;


    public AdminController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }



    @PostMapping("/register")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        //adminService.saveAdmin(admin);
        String response = adminService.saveAdmin(admin);

        if (response.startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok("Admin Created Successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminService.findByUsername(admin.getUsername());

        // Validate password using BCrypt
        if (existingAdmin.isPresent() && passwordEncoder.matches(admin.getPassword(), existingAdmin.get().getPassword())) {
            return ResponseEntity.ok("Login Successful!");
        }

        return ResponseEntity.status(401).body("Invalid Username or Password");
    }
}
