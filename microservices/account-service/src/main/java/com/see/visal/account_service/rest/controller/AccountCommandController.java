package com.see.visal.account_service.rest.controller;

import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.create.CreateAccountResponse;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyResponse;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountResponse;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyResponse;
import com.see.visal.account_service.application.service.AccountCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Account Command REST Controller
 * Simplified structure following accountQuery pattern
 * Hexagonal Architecture: REST adapter (input) → service layer → domain
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountCommandController {
    
    private final AccountCommandService accountCommandService;
    
    /**
     * Create a new account
     */
    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody CreateAccountRequest request) {
        log.info("REST: Create account for customer {}", request.customerId());
        CreateAccountResponse response = accountCommandService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Deposit money into an account
     */
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<DepositMoneyResponse> depositMoney(
            @PathVariable String accountId,
            @RequestBody DepositMoneyRequest request) {
        log.info("REST: Deposit {} to account {}", request.amount(), accountId);
        DepositMoneyResponse response = accountCommandService.depositMoney(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Withdraw money from an account
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<WithdrawMoneyResponse> withdrawMoney(
            @PathVariable String accountId,
            @RequestBody WithdrawMoneyRequest request) {
        log.info("REST: Withdraw {} from account {}", request.amount(), accountId);
        WithdrawMoneyResponse response = accountCommandService.withdrawMoney(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Freeze an account
     */
    @PutMapping("/{accountId}/freeze")
    public ResponseEntity<FreezeAccountResponse> freezeAccount(
            @PathVariable String accountId,
            @RequestBody FreezeAccountRequest request) {
        log.info("REST: Freeze account {}", accountId);
        FreezeAccountResponse response = accountCommandService.freezeAccount(request);
        return ResponseEntity.ok(response);
    }
}

