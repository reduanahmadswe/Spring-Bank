package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.service.AccountService;
import com.springbank.Spring.Bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    //balance ck dewar jonno ai ta use korci
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> checkBalance(@RequestParam String accountNumber) {
        Optional<Account> account = accountService.getAccountByAccountNumber(accountNumber);
        Optional<Customer> customer = customerService.getCustomerByAccountNumber(accountNumber);

        Map<String, Object> response = new HashMap<>();

        if (account.isPresent()) {
            response.put("accountNumber", account.get().getAccountNumber());
            response.put("Name", customer.get().getName());
            response.put("balance", account.get().getBalance());

            return ResponseEntity.ok(response);
        }

        response.put("message", "Account Not Found!");
        response.put("status", "error");

        return ResponseEntity.status(404).body(response);
    }


    @GetMapping("/customerdata")
    public ResponseEntity<Map<String, Object>> checkCustomerData(@RequestParam String accountNumber) {
        // account number onojaiye customer dekabe
        Optional<Account> account = accountService.getAccountByAccountNumber(accountNumber);
        Optional<Customer> customer = customerService.getCustomerByAccountNumber(accountNumber);

        Map<String, Object> response = new HashMap<>();

        // account exist korle data dekabe
        if (account.isPresent() && customer.isPresent()) {
            Customer cust = customer.get();

            // customer account details
            response.put("accountNumber", account.get().getAccountNumber());
            response.put("name", cust.getName());
            response.put("email", cust.getEmail());
            response.put("phone", cust.getPhone());
            response.put("balance", account.get().getBalance());
            response.put("address", cust.getAddress());
           // response.put("birthDate", cust.getBirthDate());
            response.put("fatherName", cust.getFatherName());
            response.put("gender", cust.getGender());
            response.put("motherName", cust.getMotherName());
            response.put("nationality", cust.getNationality());
            response.put("nid", cust.getNid());
            response.put("nomineeAddress", cust.getNomineeAddress());
            response.put("nomineeName", cust.getNomineeName());

            //customer data return korbe
            return ResponseEntity.ok(response);
        }

        // jodi customer account na pawa jai ta hole error message dibe
        response.put("message", "Account or Customer Not Found!");
        response.put("status", "error");

        return ResponseEntity.status(404).body(response);
    }

    // **Account Close using Account Number**
    @PutMapping("/close/{accountNumber}")
    public ResponseEntity<String> closeAccount(@PathVariable String accountNumber) {
        boolean isClosed = accountService.closeAccount(accountNumber);

        if (isClosed) {
            return ResponseEntity.ok("Account closed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Account not found or already closed.");
        }
    }


    // Request DTO for Closing Account
    public static class CloseAccountRequest {
        private Long customerId;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }

    // **Account Reopen using Account Number**
    @PostMapping("/reopen/{accountNumber}")
    public ResponseEntity<String> reopenAccount(
            @PathVariable String accountNumber,
            @RequestBody ReopenAccountRequest request) {

        Long customerId = request.getCustomerId();
        String response = accountService.reopenAccount(accountNumber, customerId);
        return ResponseEntity.ok(response);
    }

    public static class ReopenAccountRequest {
        private Long customerId;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }


}
