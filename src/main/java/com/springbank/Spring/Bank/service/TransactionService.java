package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.CustomerRepository;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public String deposit(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Account account = customer.getAccount();

            // ❌ Prevent deposit if the account is closed
            if ("closed".equals(account.getStatus())) {
                return "Transaction failed. This account is closed.";
            }

            // Old Balance Log
            System.out.println("Old Balance: " + account.getBalance());

            // Update balance
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            customer.setBalance(customer.getBalance() + amount);
            customerRepository.save(customer);  // Update customer balance

            accountRepository.flush(); // Ensure transaction is committed

            // New Balance Log
            System.out.println("New Balance: " + account.getBalance());

            // Save transaction record
            Transaction transaction = new Transaction("Deposit", amount, account);
            transactionRepository.save(transaction);

            return "Deposit successful. New balance: " + account.getBalance();
        }

        return "Customer not found!";
    }

    @Transactional
    public String withdraw(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Account account = customer.getAccount();

            // ❌ Prevent withdrawal if the account is closed
            if ("closed".equals(account.getStatus())) {
                return "Transaction failed. This account is closed.";
            }

            if (account.getBalance() >= amount) {
                // Update balance
                account.setBalance(account.getBalance() - amount);
                customer.setBalance(customer.getBalance() - amount);
                customerRepository.save(customer);
                accountRepository.save(account);

                // Save transaction
                Transaction transaction = new Transaction("Withdraw", amount, account);
                transactionRepository.save(transaction);

                return "Withdrawal successful. New balance: " + account.getBalance();
            } else {
                return "Insufficient funds!";
            }
        }
        return "Customer not found!";
    }

    @Transactional
    public String transfer(Long fromCustomerId, Long toCustomerId, double amount) {
        Optional<Customer> fromCustomerOpt = customerRepository.findById(fromCustomerId);
        Optional<Customer> toCustomerOpt = customerRepository.findById(toCustomerId);

        if (fromCustomerOpt.isPresent() && toCustomerOpt.isPresent()) {
            Customer fromCustomer = fromCustomerOpt.get();
            Customer toCustomer = toCustomerOpt.get();

            Account fromAccount = fromCustomer.getAccount();
            Account toAccount = toCustomer.getAccount();

            // ❌ Prevent transfer if either account is closed
            if ("closed".equals(fromAccount.getStatus()) || "closed".equals(toAccount.getStatus())) {
                return "Transaction failed. One or both accounts are closed.";
            }

            if (fromAccount.getBalance() >= amount) {
                // Update balance
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                // Save transactions
                Transaction transaction1 = new Transaction("Transfer", amount, fromAccount);
                transactionRepository.save(transaction1);

                Transaction transaction2 = new Transaction("Receive", amount, toAccount);
                transactionRepository.save(transaction2);

                return "Transfer successful!";
            } else {
                return "Insufficient balance!";
            }
        }
        return "One or both customers not found!";
    }

    public List<Transaction> getTransactionHistory(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            return transactionRepository.findByAccount_Id(customer.getAccount().getId());
        }
        return null;
    }
}
