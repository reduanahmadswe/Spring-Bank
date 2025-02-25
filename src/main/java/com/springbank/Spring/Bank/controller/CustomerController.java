package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;

    public CustomerController(CustomerService customerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Customer> customerOptional = customerService.getCustomerByEmail(loginRequest.getEmail());

            if (customerOptional.isEmpty()) {
                return ResponseEntity.status(401).body("Invalid email or password!");
            }

            Customer customer = customerOptional.get();
            boolean passwordValid = customerService.validatePassword(loginRequest.getPassword(), customer.getPassword());

            if (!passwordValid) {
                return ResponseEntity.status(401).body("Invalid email or password!");
            }

            // If authentication is successful, you can generate a JWT token or return a success message
            return ResponseEntity.ok("Login successful!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during login!");
        }
    }


    // Create a Customer Account
    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        try {
            System.out.println("Received customer: " + customer);
            customerService.saveCustomer(customer);
            return ResponseEntity.ok("Customer Account Created Successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Check Balance
    @GetMapping("/balance/{id}")
    public ResponseEntity<?> checkBalance(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok("{\"balance\": " + customer.get().getBalance() + "}");
        }
        return ResponseEntity.status(404).body("Customer Not Found!");
    }
}
