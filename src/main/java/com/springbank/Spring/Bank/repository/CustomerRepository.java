package com.springbank.Spring.Bank.repository;

import com.springbank.Spring.Bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
