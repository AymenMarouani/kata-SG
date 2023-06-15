package com.kata.bank.service;

import com.kata.bank.dto.AccountOperationDto;
import com.kata.bank.dto.AccountOperationRequestDto;
import com.kata.bank.dto.AccountOperationResponseDto;
import com.kata.bank.dto.AccountOperationsListingDto;
import com.kata.bank.model.AccountOperation;
import com.kata.bank.repository.AccountOperationsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.kata.bank.dto.AccountOperationResultType.FAILED;
import static com.kata.bank.dto.AccountOperationResultType.SUCCESSFUL;
import static com.kata.bank.model.AccountOperationType.SAVE;
import static com.kata.bank.model.AccountOperationType.WITHDRAW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private static final String ACCOUNT_003 = "ACCOUNT_003";

    @Mock
    private AccountOperationsRepository accountOperationsRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void getOperations_should_return_list_of_operations_when_provided_with_account_id() {
        // Given
        final var now = LocalDate.now();
        final var operation1 = new AccountOperation(1L, ACCOUNT_003, SAVE, BigDecimal.TEN, BigDecimal.TEN, now.minusDays(2));
        final var operation2 = new AccountOperation(2L, ACCOUNT_003, WITHDRAW, BigDecimal.TEN, BigDecimal.ZERO, now.minusDays(1));
        final var operation3 = new AccountOperation(3L, ACCOUNT_003, SAVE, BigDecimal.TEN, BigDecimal.TEN, now);
        final var operations = List.of(operation1, operation2, operation3);
        final var operationDto1 = new AccountOperationDto(SAVE, BigDecimal.TEN, BigDecimal.TEN, now.minusDays(2));
        final var operationDto2 = new AccountOperationDto(WITHDRAW, BigDecimal.TEN, BigDecimal.ZERO, now.minusDays(1));
        final var operationDto3 = new AccountOperationDto(SAVE, BigDecimal.TEN, BigDecimal.TEN, now);
        final var accountOperationsDto = List.of(operationDto1, operationDto2, operationDto3);
        final var expectedOperationsDto = new AccountOperationsListingDto(accountOperationsDto);
        // When
        when(accountOperationsRepository.findByAccountId(ACCOUNT_003)).thenReturn(operations);
        final var operationsDto = accountService.getOperations(ACCOUNT_003);
        // Then
        assertThat(operationsDto).isEqualTo(expectedOperationsDto);
    }

    @Test
    public void doOperation_should_return_success_result_dto_when_provided_with_account_id_for_save_operation() {
        // Given
        final var amount = BigDecimal.TEN;
        final var accountOperationRequestDto = new AccountOperationRequestDto(SAVE, amount);
        final var balance = BigDecimal.TEN;
        final var latestAccountOperation = new AccountOperation(1L, ACCOUNT_003, SAVE, BigDecimal.TEN, balance, LocalDate.now());
        final var message = String.format("Amount of %s$ successfully deposited in your account", amount);
        final var expectedBalance = balance.add(amount);
        final var expectedOperationResultDto = new AccountOperationResponseDto(SUCCESSFUL, message, expectedBalance);
        // When
        when(accountOperationsRepository.findByAccountIdLatestDate(ACCOUNT_003)).thenReturn(Optional.of(latestAccountOperation));
        when(accountOperationsRepository.save(any(AccountOperation.class))).thenReturn(null);
        final var operationResponseDto = accountService.doOperation(ACCOUNT_003, accountOperationRequestDto);
        // Then
        assertThat(operationResponseDto).isEqualTo(expectedOperationResultDto);
    }

    @Test
    public void doOperation_should_return_success_result_dto_when_provided_with_account_id_for_withdraw_operation_with_enough_balance() {
        // Given
        final var amount = BigDecimal.ONE;
        final var accountOperationRequestDto = new AccountOperationRequestDto(WITHDRAW, amount);
        final var balance = BigDecimal.TEN;
        final var latestAccountOperation = new AccountOperation(1L, ACCOUNT_003, SAVE, BigDecimal.TEN, balance, LocalDate.now());
        final var message = String.format("Amount of %s$ withdrawn from your account", amount);
        final var expectedBalance = balance.subtract(amount);
        final var expectedOperationResultDto = new AccountOperationResponseDto(SUCCESSFUL, message, expectedBalance);
        // When
        when(accountOperationsRepository.findByAccountIdLatestDate(ACCOUNT_003)).thenReturn(Optional.of(latestAccountOperation));
        when(accountOperationsRepository.save(any(AccountOperation.class))).thenReturn(null);
        final var operationResponseDto = accountService.doOperation(ACCOUNT_003, accountOperationRequestDto);
        // Then
        assertThat(operationResponseDto).isEqualTo(expectedOperationResultDto);
    }

    @Test
    public void doOperation_should_save_a_new_account_operation_when_operation_succeeds() {
        // Given
        final var amount = BigDecimal.ONE;
        final var accountOperationRequestDto = new AccountOperationRequestDto(WITHDRAW, amount);
        final var balance = BigDecimal.TEN;
        final var latestOperationDate = LocalDate.now().minusDays(2);
        final var latestAccountOperation = new AccountOperation(1L, ACCOUNT_003, SAVE, BigDecimal.TEN, balance, latestOperationDate);
        final var expectedBalance = balance.subtract(amount);
        final var operationDate = LocalDate.now();
        final var expectedAccountOperation = new AccountOperation(ACCOUNT_003, WITHDRAW, amount, expectedBalance, operationDate);
        // When
        when(accountOperationsRepository.findByAccountIdLatestDate(ACCOUNT_003)).thenReturn(Optional.of(latestAccountOperation));
        accountService.doOperation(ACCOUNT_003, accountOperationRequestDto);
        // Then
        verify(accountOperationsRepository).save(expectedAccountOperation);
    }

    @Test
    public void doOperation_should_return_failure_result_dto_when_provided_with_account_id_for_withdraw_operation_with_not_enough_balance() {
        // Given
        final var amount = BigDecimal.TEN;
        final var accountOperationRequestDto = new AccountOperationRequestDto(WITHDRAW, amount);
        final var balance = BigDecimal.ONE;
        final var latestAccountOperation = new AccountOperation(1L, ACCOUNT_003, SAVE, BigDecimal.ONE, balance, LocalDate.now());
        final var expectedOperationResultDto = new AccountOperationResponseDto(FAILED, "Insufficient account balance", balance);
        // When
        when(accountOperationsRepository.findByAccountIdLatestDate(ACCOUNT_003)).thenReturn(Optional.of(latestAccountOperation));
        final var operationResponseDto = accountService.doOperation(ACCOUNT_003, accountOperationRequestDto);
        // Then
        assertThat(operationResponseDto).isEqualTo(expectedOperationResultDto);
    }
}
