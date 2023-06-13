package com.kata.bank.controller;

import com.kata.bank.dto.AccountOperationRequestDto;
import com.kata.bank.dto.AccountOperationResponseDto;
import com.kata.bank.dto.AccountOperationsListingDto;
import com.kata.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<AccountOperationsListingDto> getAccountOperations(@PathVariable final String accountId) {
        return ResponseEntity.ok(null);
    }

    @PostMapping(path = "/{accountId}")
    public ResponseEntity<AccountOperationResponseDto> doAccountOperation(@PathVariable final String accountId
            , @RequestBody final AccountOperationRequestDto accountOperationRequestDto) {
        return ResponseEntity.ok(null);
    }
}
