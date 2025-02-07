package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Customer;
import com.springbank.Spring.Bank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer); // গ্রাহক সেভ করা
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null); // গ্রাহক আইডি দিয়ে খোঁজা
    }
}
