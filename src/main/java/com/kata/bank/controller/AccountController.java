package com.kata.bank.controller;

import com.kata.bank.dto.AccountOperationRequestDto;
import com.kata.bank.dto.AccountOperationResponseDto;
import com.kata.bank.dto.AccountOperationsListingDto;
import com.kata.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<AccountOperationsListingDto> getAccountOperations(@PathVariable final String accountId) {
        final var result = accountService.getOperations(accountId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/{accountId}")
    public ResponseEntity<AccountOperationResponseDto> doAccountOperation(@PathVariable final String accountId
            , @Valid @RequestBody final AccountOperationRequestDto accountOperationRequestDto) {
        final var result = accountService.doOperation(accountId, accountOperationRequestDto);
        return ResponseEntity.ok(result);
    }
}
