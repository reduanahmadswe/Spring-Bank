package com.springbank.Spring.Bank.controller;


import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import com.springbank.Spring.Bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        return transactionService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        return transactionService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam double amount) {
        return transactionService.transfer(fromAccountNumber, toAccountNumber, amount);
    }

    // Transaction History API using Account Number
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        // Calling the service method to get transaction history by account number
        List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }

}

