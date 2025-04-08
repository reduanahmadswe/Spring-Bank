package com.springbank.Spring.Bank.repository;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Transaction;
import jakarta.persistence.criteria.Fetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   long countByAccountAndStatus(Account account, String status);
   List<Transaction> findByAccount_AccountNumber(String accountNumber);


 // Additional Query: Fetch transactions by Account Number with sorting (for most recent transactions first)
   @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber ORDER BY t.timestamp DESC")
   List<Transaction> findByAccountNumber(@Param("accountNumber") String accountNumber);

   // Example: Counting transactions by account number and status (e.g., completed)
   long countByAccount_AccountNumberAndStatus(String accountNumber, String status);
}
