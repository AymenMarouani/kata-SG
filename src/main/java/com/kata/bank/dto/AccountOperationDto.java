package com.kata.bank.dto;

import com.kata.bank.model.AccountOperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountOperationDto {

    private final AccountOperationType type;

    private final BigDecimal amount;

    private final BigDecimal balance;

    private final LocalDate date;

    public AccountOperationDto(AccountOperationType type, BigDecimal amount, BigDecimal balance, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
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
