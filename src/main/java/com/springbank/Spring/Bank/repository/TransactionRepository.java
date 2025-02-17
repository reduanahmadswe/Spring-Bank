package com.springbank.Spring.Bank.repository;

import com.springbank.Spring.Bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   List<Transaction> findByAccount_Id(Long accountId);
}
