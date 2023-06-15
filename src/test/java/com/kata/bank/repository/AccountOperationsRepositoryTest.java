package com.kata.bank.repository;

import com.kata.bank.model.AccountOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kata.bank.model.AccountOperationType.SAVE;
import static com.kata.bank.model.AccountOperationType.WITHDRAW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountOperationsRepositoryTest {

    private static final String ACCOUNT_001 = "ACCOUNT_001";
    private static final String ACCOUNT_003 = "ACCOUNT_003";
    private static final LocalDate now = LocalDate.now();

    private final Map<Long, AccountOperation> persistedOperations = Map.of(
            1L, new AccountOperation(1L, ACCOUNT_001, SAVE,
                    BigDecimal.TEN, BigDecimal.TEN, now.minusDays(3)),
            2L, new AccountOperation(2L, ACCOUNT_001, WITHDRAW,
                    BigDecimal.ONE, BigDecimal.valueOf(9), now.minusDays(1)),
            3L, new AccountOperation(3L, ACCOUNT_003, SAVE,
                    BigDecimal.valueOf(25), BigDecimal.valueOf(25), now.minusDays(3)),
            4L, new AccountOperation(4L, ACCOUNT_001, SAVE,
                    BigDecimal.TEN, BigDecimal.valueOf(19), now),
            5L, new AccountOperation(5L, ACCOUNT_003, SAVE,
                    BigDecimal.TEN, BigDecimal.valueOf(35), now)
    );

    @Mock
    private AccountDataBase accountDataBase;

    @InjectMocks
    private AccountOperationsRepository accountOperationsRepository;

    @Test
    public void findByAccountId_should_return_list_of_account_operations_sorted_by_date_when_provided_with_account_id() {
        // Given
        final var expectedOperations = List.of(
                new AccountOperation(3L, ACCOUNT_003, SAVE, BigDecimal.valueOf(25), BigDecimal.valueOf(25), now.minusDays(3)),
                new AccountOperation(5L, ACCOUNT_003, SAVE, BigDecimal.TEN, BigDecimal.valueOf(35), now)
        );
        // When
        when(accountDataBase.getAccountOperation()).thenReturn(persistedOperations);
        final var operations = accountOperationsRepository.findByAccountId(ACCOUNT_003);
        // Then
        assertThat(operations).isEqualTo(expectedOperations);
    }

    @Test
    public void findByAccountIdLatestDate_should_return_latest_account_operation_when_provided_with_account_id() {
        // Given
        final var expectedOperation = new AccountOperation(4L, ACCOUNT_001, SAVE, BigDecimal.TEN, BigDecimal.valueOf(19), now);
        // When
        when(accountDataBase.getAccountOperation()).thenReturn(persistedOperations);
        final var latestOperation = accountOperationsRepository.findByAccountIdLatestDate(ACCOUNT_001);
        // Then
        assertThat(latestOperation).isEqualTo(Optional.of(expectedOperation));
    }

    @Test
    public void save_should_return_newly_saved_account_entity_operation_when_provided_with_a_new_one() {
        // Given
        final var operation = new AccountOperation(ACCOUNT_003, SAVE, BigDecimal.TEN, BigDecimal.valueOf(45), now);
        final var expectedSavedOperation = new AccountOperation(6L, ACCOUNT_003, SAVE, BigDecimal.TEN, BigDecimal.valueOf(45), now);
        // When
        when(accountOperationsRepository.save(operation)).thenReturn(expectedSavedOperation);
        final var savedOperation = accountOperationsRepository.save(operation);
        // Then
        assertThat(savedOperation).isEqualTo(expectedSavedOperation);
    }

    @Test
    public void save_should_return_updated_and_persisted_account_entity_operation_when_provided_with_an_already_existing_one() {
        // Given
        final var operation = new AccountOperation(4L, ACCOUNT_001, SAVE, BigDecimal.ONE, BigDecimal.TEN, now);
        final var expectedSavedOperation = new AccountOperation(4L, ACCOUNT_001, SAVE, BigDecimal.ONE, BigDecimal.TEN, now);
        // When
        when(accountOperationsRepository.save(operation)).thenReturn(expectedSavedOperation);
        final var savedOperation = accountOperationsRepository.save(operation);
        // Then
        assertThat(savedOperation).isEqualTo(expectedSavedOperation);
    }
}
