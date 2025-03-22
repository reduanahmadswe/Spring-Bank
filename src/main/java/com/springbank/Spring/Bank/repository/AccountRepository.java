package com.springbank.Spring.Bank.repository;

import com.springbank.Spring.Bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumberAndCustomerId(String accountNumber, Long customerId);
    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);

}
