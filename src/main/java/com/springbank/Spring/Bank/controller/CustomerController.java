package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create a Customer Account
    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        try {
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
