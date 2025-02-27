package com.springbank.Spring.Bank.controller;

import com.springbank.Spring.Bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // **Account Close using Account Number**
    @PostMapping("/close/{accountNumber}")
    public ResponseEntity<String> closeAccount(
            @PathVariable String accountNumber,
            @RequestBody CloseAccountRequest request) {

        Long customerId = request.getCustomerId();
        String response = accountService.closeAccount(accountNumber, customerId);
        return ResponseEntity.ok(response);
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
}
