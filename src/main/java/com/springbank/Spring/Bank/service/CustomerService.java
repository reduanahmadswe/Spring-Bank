package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);  // Save customer

        // Create new Account
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(UUID.randomUUID().toString()); // Set unique account number
        account.setBalance(0.0);

        accountRepository.save(account);  // Save account
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);  // Find customer by ID
    }
}
