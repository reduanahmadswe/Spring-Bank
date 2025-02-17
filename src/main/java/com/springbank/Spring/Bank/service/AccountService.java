package com.springbank.Spring.Bank.service;

import com.springbank.Spring.Bank.model.Account;
import com.springbank.Spring.Bank.model.Transaction;
import com.springbank.Spring.Bank.repository.AccountRepository;
import com.springbank.Spring.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public String closeAccount(Long accountId, Long customerId) {
        System.out.println("AccountId: " + accountId + ", CustomerId: " + customerId);

        // Find account by ID and check ownership
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getCustomer().getId().equals(customerId))
                .orElse(null);

        if (account == null) {
            return "Account not found or unauthorized access.";
        }

        // If already closed, prevent further closure
        if ("closed".equals(account.getStatus())) {
            return "This account is already closed.";
        }

        // Ensure account balance is zero before closure
        if (account.getBalance() > 0) {
            return "Account balance must be zero to close the account.";
        }

        // Ensure no pending transactions exist
        long pendingTransactions = transactionRepository.countByAccountAndStatus(account, "pending");
        if (pendingTransactions > 0) {
            return "Please complete all pending transactions before closing the account.";
        }

        // Mark the account as closed
        account.setStatus("closed");
        account.setCloseAt(LocalDateTime.now());
        accountRepository.save(account);

        // Log account closure as a transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(0.0);
        transaction.setType("Account Closure");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Account closed successfully. No further transactions allowed.";
    }

    public String reopenAccount(Long accountId, Long customerId) {
        // গ্রাহকের অ্যাকাউন্ট খোঁজা
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            // চেক করুন গ্রাহক এই অ্যাকাউন্টের মালিক কিনা
            if (!account.getCustomer().getId().equals(customerId)) {
                return "Unauthorized access! This account does not belong to the provided customer.";
            }

            // চেক করুন অ্যাকাউন্টটি বন্ধ রয়েছে কিনা
            if (!"closed".equals(account.getStatus())) {
                return "Account is already active.";
            }

            // চেক করুন কোনও পেন্ডিং ট্রানজেকশন আছে কিনা
            long pendingTransactions = transactionRepository.countByAccountAndStatus(account, "pending");
            if (pendingTransactions > 0) {
                return "Account cannot be reopened while there are pending transactions.";
            }

            // অ্যাকাউন্ট পুনরায় চালু করা হচ্ছে
            account.setStatus("active");
            account.setCloseAt(null); // বন্ধ করার সময় মুছে ফেলুন
            accountRepository.save(account);

            return "Account reopened successfully.";
        }
        return "Account not found!";
    }


}
