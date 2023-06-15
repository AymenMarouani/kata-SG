package com.kata.bank.dto;

import com.kata.bank.model.AccountOperationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class AccountOperationRequestDto {

    @NotNull
    private final AccountOperationType type;

    @NotNull
    @Positive
    private final BigDecimal amount;

    public AccountOperationRequestDto(final AccountOperationType type, final BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    public AccountOperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
