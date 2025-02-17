package com.springbank.Spring.Bank.repository;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   long countByAccount_AccountNumberAndStatus(String accountNumber, String status);
   List<Transaction> findByAccount_Id(Long accountId);
   long countByAccountAndStatus(Account account, String status);

}
