package com.springbank.Spring.Bank.service;


import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.model.Transaction;
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
    private TransactionRepository transactionRepository;

    @Transactional
    public String deposit(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setBalance(customer.getBalance() + amount);
            customerRepository.save(customer);

            // Save transaction log
            transactionRepository.save(new Transaction("Deposit", amount, customer));

            return "Deposit successful. New balance: " + customer.getBalance();
        }
        return "Customer not found!";
    }

    @Transactional
    public String withdraw(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getBalance() >= amount) {
                customer.setBalance(customer.getBalance() - amount);
                customerRepository.save(customer);

                // Save transaction log
                transactionRepository.save(new Transaction("Withdraw", amount, customer));

                return "Withdrawal successful. New balance: " + customer.getBalance();
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

            if (fromCustomer.getBalance() >= amount) {
                fromCustomer.setBalance(fromCustomer.getBalance() - amount);
                toCustomer.setBalance(toCustomer.getBalance() + amount);

                customerRepository.save(fromCustomer);
                customerRepository.save(toCustomer);

                // Save transaction log
                transactionRepository.save(new Transaction("Transfer", amount, fromCustomer));
                transactionRepository.save(new Transaction("Receive", amount, toCustomer));

                return "Transfer successful!";
            } else {
                return "Insufficient balance!";
            }
        }
        return "One or both customers not found!";
    }

    public List<Transaction> getTransactionHistory(Long customerId) {
        return transactionRepository.findByCustomer_Id(customerId);
    }
}

