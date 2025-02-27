package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;

    public CustomerController(CustomerService customerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Customer> customerOptional = customerService.getCustomerByEmail(loginRequest.getEmail());

            if (customerOptional.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password!"));
            }

            Customer customer = customerOptional.get();
            boolean passwordValid = customerService.validatePassword(loginRequest.getPassword(), customer.getPassword());

            if (!passwordValid) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password!"));
            }

            String jwtToken = customerService.generateJwtToken(customer);

            // JWT token response-এ পাঠানো হচ্ছে JSON format-এ
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("token", jwtToken);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();  // Error trace print করবেন, যাতে ডিবাগিং সহজ হয়
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during login!"));
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
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<?> checkBalance(@PathVariable String accountNumber) {
        Optional<Customer> customer = customerService.getCustomerByAccountNumber(accountNumber);

        if (customer.isPresent()) {
            return ResponseEntity.ok("{\"balance\": " + customer.get().getBalance() + "}");
        }
        return ResponseEntity.status(404).body("Customer Not Found!");
    }

}
