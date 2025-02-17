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

    @PostMapping("/close/{accountId}")
    public ResponseEntity<String> closeAccount(@PathVariable Long accountId, @RequestBody CloseAccountRequest request) {
        Long customerId = request.getCustomerId();
        String response = accountService.closeAccount(accountId, customerId);
        return ResponseEntity.ok(response);
    }

    public static class CloseAccountRequest {
        private Long customerId;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }

    @PostMapping("/reopen/{accountId}")
    public ResponseEntity<String> reopenAccount(
            @PathVariable Long accountId,
            @RequestBody ReopenAccountRequest request) {

        Long customerId = request.getCustomerId();
        String response = accountService.reopenAccount(accountId, customerId);
        return ResponseEntity.ok(response);
    }

    // গ্রাহকের অনুরোধ গ্রহণের জন্য একটি ক্লাস
    public static class ReopenAccountRequest {
        private Long customerId;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }


    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
