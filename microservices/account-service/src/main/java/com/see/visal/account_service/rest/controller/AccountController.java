package com.see.visal.account_service.rest.controller;

import com.see.visal.account_service.application.AccountService;
import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.create.CreateAccountResponse;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyResponse;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountResponse;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /** POST /api/accounts — Create a new account */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateAccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest) {
        log.info("Create account: {}", createAccountRequest);
        return accountService.createAccount(createAccountRequest);
    }

    /** POST /api/accounts/deposit — Deposit money into an account */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/deposit")
    public DepositMoneyResponse depositMoney(
            @Valid @RequestBody DepositMoneyRequest depositMoneyRequest) {
        log.info("Deposit money request: {}", depositMoneyRequest);
        return accountService.depositMoney(depositMoneyRequest);
    }

    /** POST /api/accounts/withdraw — Withdraw money from an account */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/withdraw")
    public WithdrawMoneyResponse withdrawMoney(
            @Valid @RequestBody WithdrawMoneyRequest withdrawMoneyRequest) {
        log.info("Withdraw money request: {}", withdrawMoneyRequest);
        return accountService.withdrawMoney(withdrawMoneyRequest);
    }

    /** POST /api/accounts/freeze — Freeze an account */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/freeze")
    public FreezeAccountResponse freezeAccount(
            @Valid @RequestBody FreezeAccountRequest freezeAccountRequest) {
        log.info("Freeze account request: {}", freezeAccountRequest);
        return accountService.freezeAccount(freezeAccountRequest);
    }
}
