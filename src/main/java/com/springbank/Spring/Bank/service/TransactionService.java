package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public String deposit(String accountNumber, double amount) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            // Check if the account is closed
            if ("closed".equalsIgnoreCase(account.getStatus())) {
                return "Transaction failed. This account is closed. Transactions are not allowed.";
            }

            // Proceed with deposit if the account is active
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            // Record the transaction
            Transaction transaction = new Transaction("Deposit", amount, account);
            transactionRepository.save(transaction);

            return "Deposit successful. New balance: " + account.getBalance();
        }

        return "Account not found!";
    }

    @Transactional
    public String withdraw(String accountNumber, double amount) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            // Check if the account is closed
            if ("closed".equalsIgnoreCase(account.getStatus())) {
                return "Transaction failed. This account is closed.";
            }

            // Check if there are sufficient funds
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);

                Transaction transaction = new Transaction("Withdraw", amount, account);
                transactionRepository.save(transaction);

                return "Withdrawal successful. New balance: " + account.getBalance();
            } else {
                return "Insufficient funds!";
            }
        }
        return "Account not found!";
    }

    @Transactional
    public String transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Optional<Account> fromAccountOpt = accountRepository.findByAccountNumber(fromAccountNumber);
        Optional<Account> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber);

        if (fromAccountOpt.isPresent() && toAccountOpt.isPresent()) {
            Account fromAccount = fromAccountOpt.get();
            Account toAccount = toAccountOpt.get();

            // Check if either account is closed
            if ("closed".equalsIgnoreCase(fromAccount.getStatus()) || "closed".equalsIgnoreCase(toAccount.getStatus())) {
                return "Transaction failed. One or both accounts are closed.";
            }

            // Check if the sender has enough balance
            if (fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                Transaction transaction1 = new Transaction("Transfer", amount, fromAccount);
                transactionRepository.save(transaction1);

                Transaction transaction2 = new Transaction("Receive", amount, toAccount);
                transactionRepository.save(transaction2);

                return "Transfer successful!";
            } else {
                return "Insufficient balance!";
            }
        }
        return "One or both accounts not found!";
    }



    // Transaction History deker jonno
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccount_AccountNumber(accountNumber);
    }

    // balance get korar jonno
    public double getBalance(String accountNumber) {
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance(); // Accessing the balance from the Account object
        } else {
            throw new RuntimeException("Account not found");
        }
    }
}