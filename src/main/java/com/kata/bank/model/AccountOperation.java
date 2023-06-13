package com.kata.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountOperation {

    private final long id;

    private final String accountId;

    private final AccountOperationType type;

    private final BigDecimal amount;

    private final BigDecimal balance;

    private final LocalDate date;

    public AccountOperation(long id, String accountId, AccountOperationType type, BigDecimal amount, BigDecimal balance, LocalDate date) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public AccountOperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDate getDate() {
        return date;
    }
}
