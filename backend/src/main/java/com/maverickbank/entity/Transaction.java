package com.maverickbank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private String description;

    public Transaction() {}
    public Transaction(Long id, String type, BigDecimal amount, LocalDateTime date, Account account, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.description = description;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
