package com.kata.bank.repository;

import com.kata.bank.model.AccountOperation;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountOperationsRepository {

    private final AccountDataBase accountDataBase;

    public AccountOperationsRepository(final AccountDataBase accountDataBase) {
        this.accountDataBase = accountDataBase;
    }

    public List<AccountOperation> findByAccountId(final String accountId) {
        final var accountOperations = accountDataBase.getAccountOperation();
        return accountOperations.values().stream()
                .filter(operation -> accountId.equals(operation.getAccountId()))
                .sorted(Comparator.comparing(AccountOperation::getDate))
                .collect(Collectors.toList());
    }

    public Optional<AccountOperation> findByAccountIdLatestDate(final String accountId) {
        final var accountOperations = accountDataBase.getAccountOperation();
        return accountOperations.values().stream()
                .filter(operation -> accountId.equals(operation.getAccountId()))
                .max(Comparator.comparing(AccountOperation::getDate));
    }

    public AccountOperation save(final AccountOperation accountOperation) {
        return accountDataBase.save(accountOperation);
    }
}
