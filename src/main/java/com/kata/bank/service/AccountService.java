package com.kata.bank.service;

import com.kata.bank.dto.AccountOperationDto;
import com.kata.bank.dto.AccountOperationRequestDto;
import com.kata.bank.dto.AccountOperationResponseDto;
import com.kata.bank.dto.AccountOperationsListingDto;
import com.kata.bank.model.AccountOperation;
import com.kata.bank.repository.AccountOperationsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.kata.bank.dto.AccountOperationResultType.FAILED;
import static com.kata.bank.dto.AccountOperationResultType.SUCCESSFUL;

@Service
public class AccountService {

    private final AccountOperationsRepository accountOperationsRepository;

    public AccountService(final AccountOperationsRepository accountOperationsRepository) {
        this.accountOperationsRepository = accountOperationsRepository;
    }

    public AccountOperationsListingDto getOperations(final String accountId) {
        final var accountOperations = accountOperationsRepository.findByAccountId(accountId);
        final var operations = accountOperations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new AccountOperationsListingDto(operations);
    }

    public AccountOperationResponseDto doOperation(final String accountId, final AccountOperationRequestDto accountOperationRequestDto) {
        final var accountOperationResult = getAccountOperationResult(accountId, accountOperationRequestDto);
        if (accountOperationResult.getResult() == SUCCESSFUL) {
            final var accountOperation = createAccountOperation(accountId, accountOperationRequestDto, accountOperationResult);
            accountOperationsRepository.save(accountOperation);
        }
        return accountOperationResult;
    }

    private AccountOperationDto mapToDto(AccountOperation accountOperation) {
        final var type = accountOperation.getType();
        final var amount = accountOperation.getAmount();
        final var balance = accountOperation.getBalance();
        final var date = accountOperation.getDate();
        return new AccountOperationDto(type, amount, balance, date);
    }

    private AccountOperationResponseDto getAccountOperationResult(final String accountId
            , final AccountOperationRequestDto accountOperationRequestDto) {
        final var accountBalance = getAccountBalance(accountId);
        final var amount = accountOperationRequestDto.getAmount();
        switch (accountOperationRequestDto.getType()) {
            case WITHDRAW: {
                final var updatedBalance = accountBalance.subtract(amount);
                if (updatedBalance.signum() < 0) {
                    return new AccountOperationResponseDto(FAILED, "Insufficient account balance", accountBalance);
                }
                final var message = String.format("Amount of %s$ withdrawn from your account", amount);
                return new AccountOperationResponseDto(SUCCESSFUL, message, updatedBalance);
            }
            case SAVE:
            default: {
                final var updatedBalance = accountBalance.add(amount);
                final var message = String.format("Amount of %s$ successfully deposited in your account", amount);
                return new AccountOperationResponseDto(SUCCESSFUL, message, updatedBalance);
            }
        }
    }

    private AccountOperation createAccountOperation(final String accountId, final AccountOperationRequestDto accountOperationRequestDto
            , final AccountOperationResponseDto accountOperationResult) {
        final var type = accountOperationRequestDto.getType();
        final var amount = accountOperationRequestDto.getAmount();
        final var balance = accountOperationResult.getBalance();
        final var date = LocalDate.now();
        return new AccountOperation(accountId, type, amount, balance, date);
    }

    private BigDecimal getAccountBalance(String accountId) {
        final var latestAccountOperation = accountOperationsRepository.findByAccountIdLatestDate(accountId);
        return latestAccountOperation.map(AccountOperation::getBalance)
                .orElse(BigDecimal.ZERO);
    }
}
