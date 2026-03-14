package com.see.visal.account_service.application.service;

import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.create.CreateAccountResponse;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyResponse;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountResponse;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyResponse;
import com.see.visal.account_service.application.mapper.AccountApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Account Command Service
 * Hexagonal Architecture: Application layer that orchestrates commands
 * Acts as bridge between REST adapter and domain/command bus
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountCommandService {
    
    private final CommandGateway commandGateway;
    private final AccountApplicationMapper accountMapper;
    
    /**
     * Create a new account
     * Port: converts DTO → Command → sends via Axon CommandGateway
     */
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        log.info("Service: Creating account for customer {}", request.customerId());
        
        var command = accountMapper.toCreateAccountCommand(request);
        commandGateway.sendAndWait(command);
        
        // Build response
        return CreateAccountResponse.builder()
                .accountId(command.accountId().getValue())
                .accountNumber(command.accountNumber())
                .message("Account created successfully")
                .build();
    }
    
    /**
     * Deposit money into an account
     */
    public DepositMoneyResponse depositMoney(DepositMoneyRequest request) {
        log.info("Service: Depositing {} to account {}", request.amount(), request.accountId());
        
        var command = accountMapper.toDepositMoneyCommand(request);
        commandGateway.sendAndWait(command);
        
        return DepositMoneyResponse.builder()
                .accountId(command.accountId().getValue())
                .transactionId(command.transactionId().getValue())
                .depositedAmount(command.amount().amount())
                .currency(command.amount().currency().code())
                .message("Money deposited successfully")
                .build();
    }
    
    /**
     * Withdraw money from an account
     */
    public WithdrawMoneyResponse withdrawMoney(WithdrawMoneyRequest request) {
        log.info("Service: Withdrawing {} from account {}", request.amount(), request.accountId());
        
        var command = accountMapper.toWithdrawMoneyCommand(request);
        commandGateway.sendAndWait(command);
        
        return WithdrawMoneyResponse.builder()
                .accountId(command.accountId().getValue())
                .transactionId(command.transactionId().getValue())
                .withdrawnAmount(command.amount().amount())
                .currency(command.amount().currency().code())
                .message("Money withdrawn successfully")
                .build();
    }
    
    /**
     * Freeze an account
     */
    public FreezeAccountResponse freezeAccount(FreezeAccountRequest request) {
        log.info("Service: Freezing account {}", request.accountId());
        
        var command = accountMapper.toFreezeAccountCommand(request);
        commandGateway.sendAndWait(command);
        
        return FreezeAccountResponse.builder()
                .accountId(command.accountId().getValue())
                .accountStatus("FROZEN")
                .reason(command.remark())
                .message("Account frozen successfully")
                .build();
    }
}

