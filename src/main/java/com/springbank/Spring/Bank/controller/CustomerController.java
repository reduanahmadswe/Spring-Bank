package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Customer Account Creation
    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer); // নতুন গ্রাহক তৈরি
        return ResponseEntity.ok("Customer Account Created Successfully!");
    }

    // Balance Check
    @GetMapping("/balance/{id}")
    public ResponseEntity<String> checkBalance(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok("{\"balance\": " + customer.getBalance() + "}");
        }
        return ResponseEntity.status(404).body("Customer Not Found!");
    }
}
