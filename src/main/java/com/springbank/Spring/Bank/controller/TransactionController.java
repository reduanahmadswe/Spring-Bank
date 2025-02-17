package com.springbank.Spring.Bank.controller;


import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public String deposit(@RequestParam Long customerId, @RequestParam double amount) {
        return transactionService.deposit(customerId, amount);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long customerId, @RequestParam double amount) {
        return transactionService.withdraw(customerId, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromCustomerId, @RequestParam Long toCustomerId, @RequestParam double amount) {
        return transactionService.transfer(fromCustomerId, toCustomerId, amount);
    }

    @GetMapping("/history/{customerId}")
    public List<Transaction> getTransactionHistory(@PathVariable Long customerId) {
        return transactionService.getTransactionHistory(customerId);
    }
}

