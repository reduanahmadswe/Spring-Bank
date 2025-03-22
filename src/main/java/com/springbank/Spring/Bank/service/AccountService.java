package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.controller.AccountController;
import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.CustomerRepository;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // **Generate Unique Account Number**
    public String createUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        long randomDigits = (long) (Math.pow(10, 9) + random.nextLong() % (Math.pow(10, 10) - Math.pow(10, 9)));
        return "232" + randomDigits;
    }

    // **Close Account using Account Number**
    // Account close using account number
    public boolean closeAccount(String accountNumber) {
        // Account খোঁজা হচ্ছে account number দিয়ে
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // অ্যাকাউন্টটি 'active' হলে সেটির স্ট্যাটাস 'Closed' করা হচ্ছে
            if ("active".equals(account.getStatus())) {
                account.setStatus("Closed");
                account.setCloseAt(LocalDateTime.now());  // Close time সেট করা হচ্ছে
                accountRepository.save(account);  // পরিবর্তন সেভ করা হচ্ছে
                return true;
            }
        }
        return false;
    }

    // **Reopen Account using Account Number**
    public String reopenAccount(String accountNumber, Long customerId) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();


            if (!account.getCustomer().getId().equals(customerId)) {
                return "Unauthorized access! This account does not belong to the provided customer.";
            }


            if (!"closed".equalsIgnoreCase(account.getStatus())) {
                return "Account is already active.";
            }


            long pendingTransactions = transactionRepository.countByAccountAndStatus(account, "pending");
            if (pendingTransactions > 0) {
                return "Account cannot be reopened while there are pending transactions.";
            }


            account.setStatus("active");
            account.setCloseAt(null);  // Close date reset
            accountRepository.save(account);

            return "Account reopened successfully.";
        }
        return "Account not found!";
    }


    public void updateBalance(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double currentBalance = account.getBalance();
        double newBalance = currentBalance + amount;
        account.setBalance(newBalance);

        // Update customer's balance if needed
        Customer customer = account.getCustomer();
        if (customer != null) {
            double customerBalance = customer.getBalance();
            double newCustomerBalance = customerBalance + amount;
            customer.setBalance(newCustomerBalance);
            customerRepository.save(customer);
        }

        accountRepository.save(account);
    }


    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }




}
