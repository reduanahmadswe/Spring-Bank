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

            // Old Balance Log
            System.out.println("Old Balance: " + account.getBalance());

            // Update balance
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            customer.setBalance(customer.getBalance() + amount);
            customerRepository.save(customer);  // Customer টেবিলের ব্যালান্স আপডেট

            accountRepository.flush(); // Ensure the transaction is committed

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
            Account account = customer.getAccount();  // গ্রাহকের অ্যাকাউন্ট পাওয়া

            if (account.getBalance() >= amount) {
                // ব্যালান্স আপডেট
                account.setBalance(account.getBalance() - amount);
                customer.setBalance(customer.getBalance() - amount);
                customerRepository.save(customer);
                accountRepository.save(account);  // অ্যাকাউন্ট আপডেট

                // ট্রানজেকশন সেভ
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

            if (fromAccount.getBalance() >= amount) {
                // ব্যালান্স আপডেট
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                // ট্রানজেকশন সেভ
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
            return transactionRepository.findByAccount_Id(customer.getAccount().getId());  // অ্যাকাউন্ট আইডি দিয়ে লেনদেন খোঁজা
        }
        return null;
    }
}


