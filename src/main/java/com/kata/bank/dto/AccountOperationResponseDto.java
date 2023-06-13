package com.kata.bank.dto;

import java.math.BigDecimal;

public class AccountOperationResponseDto {

    private final AccountOperationResultType result;

    private final String message;

    private final BigDecimal balance;

    public AccountOperationResponseDto(AccountOperationResultType result, String message, BigDecimal balance) {
        this.result = result;
        this.message = message;
        this.balance = balance;
    }

    public AccountOperationResultType getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
