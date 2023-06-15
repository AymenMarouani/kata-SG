package com.kata.bank.repository;

import com.kata.bank.model.AccountOperation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.kata.bank.model.AccountOperationType.SAVE;
import static com.kata.bank.model.AccountOperationType.WITHDRAW;

@Component
public class AccountDataBase {

    public static final String ACCOUNT_001 = "ACCOUNT_001";
    public static final String ACCOUNT_002 = "ACCOUNT_002";

    private final Map<Long, AccountOperation> account_operation = new HashMap<>();

    private final AtomicLong account_operation_sequence = new AtomicLong(1);

    public AccountDataBase() {
        LocalDate now = LocalDate.now();
        final var operation1 = new AccountOperation(getOperationSequenceAndIncrement(), ACCOUNT_001, SAVE,
                BigDecimal.TEN, BigDecimal.TEN, now.minusDays(3));
        final var operation4 = new AccountOperation(getOperationSequenceAndIncrement(), ACCOUNT_001, WITHDRAW,
                BigDecimal.ONE, BigDecimal.valueOf(9), now.minusDays(1));
        final var operation5 = new AccountOperation(getOperationSequenceAndIncrement(), ACCOUNT_001, SAVE,
                BigDecimal.TEN, BigDecimal.valueOf(19), now);
        final var operation2 = new AccountOperation(getOperationSequenceAndIncrement(), ACCOUNT_002, SAVE,
                BigDecimal.valueOf(25), BigDecimal.valueOf(25), now.minusDays(3));
        final var operation3 = new AccountOperation(getOperationSequenceAndIncrement(), ACCOUNT_002, SAVE,
                BigDecimal.TEN, BigDecimal.valueOf(35), now);
        account_operation.put(operation1.getId(), operation1);
        account_operation.put(operation2.getId(), operation2);
        account_operation.put(operation3.getId(), operation3);
        account_operation.put(operation4.getId(), operation4);
        account_operation.put(operation5.getId(), operation5);
    }

    public Map<Long, AccountOperation> getAccountOperation() {
        return Collections.unmodifiableMap(account_operation);
    }

    public AccountOperation save(final AccountOperation accountOperation) {
        final var id = Optional.ofNullable(accountOperation.getId()).orElseGet(this::getOperationSequenceAndIncrement);
        final var accountId = accountOperation.getAccountId();
        final var type = accountOperation.getType();
        final var amount = accountOperation.getAmount();
        final var balance = accountOperation.getBalance();
        final var date = accountOperation.getDate();
        final var savedAccountOperation = new AccountOperation(id, accountId, type, amount, balance, date);
        account_operation.put(id, savedAccountOperation);
        return savedAccountOperation;
    }

    private Long getOperationSequenceAndIncrement() {
        return account_operation_sequence.getAndIncrement();
    }
}
