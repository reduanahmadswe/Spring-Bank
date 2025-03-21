package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.CustomerRepository;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public String closeAccount(String accountNumber, Long customerId) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .filter(acc -> acc.getCustomer().getId().equals(customerId))
                .orElse(null);

        if (account == null) {
            return "Account not found or unauthorized access.";
        }

        if ("closed".equalsIgnoreCase(account.getStatus())) {
            return "This account is already closed.";
        }

        if (account.getBalance() > 0) {
            return "Account balance must be zero to close the account.";
        }

        long pendingTransactions = transactionRepository.countByAccountAndStatus(account, "pending");
        if (pendingTransactions > 0) {
            return "Please complete all pending transactions before closing the account.";
        }

        account.setStatus("closed");
        account.setCloseAt(LocalDateTime.now());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(0.0);
        transaction.setType("Account Closure");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Account closed successfully. No further transactions allowed.";
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

}
