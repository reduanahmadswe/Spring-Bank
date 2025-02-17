package com.springbank.Spring.Bank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springbank.Spring.Bank.model.Customer;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private String type; // ডিপোজিট, উইথড্র, ট্রান্সফার
    private Double amount;
    private LocalDateTime timestamp;
    private String status = "completed";

    @ManyToOne
    @JoinColumn(name = "account_id")  // অ্যাকাউন্টের সাথে সম্পর্ক
    @JsonIgnoreProperties({"customer", "accountNumber", "balance"})
    private Account account; // লেনদেন করা অ্যাকাউন্ট

    // কনস্ট্রাক্টর
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String type, Double amount, Account account) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // গেটার এবং সেটার মেথড
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

