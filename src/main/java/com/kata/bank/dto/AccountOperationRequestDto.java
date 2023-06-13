package com.kata.bank.dto;

import com.kata.bank.model.AccountOperationType;

import java.math.BigDecimal;

public class AccountOperationRequestDto {

    private final AccountOperationType operationType;

    private final BigDecimal amount;

    public AccountOperationRequestDto(AccountOperationType operationType, BigDecimal amount) {
        this.operationType = operationType;
        this.amount = amount;
    }

    public AccountOperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
