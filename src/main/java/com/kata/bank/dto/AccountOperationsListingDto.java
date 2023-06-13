package com.kata.bank.dto;

import java.time.LocalDate;
import java.util.Map;

public class AccountOperationsListingDto {

    private final Map<LocalDate, AccountOperationDto> operations;

    public AccountOperationsListingDto(Map<LocalDate, AccountOperationDto> operations) {
        this.operations = operations;
    }

    public Map<LocalDate, AccountOperationDto> getOperations() {
        return Map.copyOf(operations);
    }
}
