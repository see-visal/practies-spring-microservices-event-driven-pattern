package com.see.visal.account_service.application;

import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.create.CreateAccountResponse;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyResponse;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountResponse;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyResponse;
import com.see.visal.account_service.application.mapper.AccountApplicationMapper;
import com.see.visal.account_service.data.entity.AccountEntity;
import com.see.visal.account_service.data.repository.AccountRepository;
import com.see.visal.account_service.domain.command.CreateAccountCommand;
import com.see.visal.account_service.domain.command.DepositMoneyCommand;
import com.see.visal.account_service.domain.command.FreezeAccountCommand;
import com.see.visal.account_service.domain.command.WithdrawMoneyCommand;
import com.see_visal.common.domain.valueoject.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final CommandGateway commandGateway;
    private final AccountApplicationMapper mapper;
    private final AccountRepository accountRepository;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for customer: {}", request.customerId());

        CreateAccountCommand cmd = mapper.toCreateAccountCommand(request);
        AccountId accountId = commandGateway.sendAndWait(cmd);

        return CreateAccountResponse.builder()
                .accountId(accountId.getValue())
                .accountNumber(cmd.accountNumber())
                .message("Account created successfully")
                .build();
    }

    @Override
    public DepositMoneyResponse depositMoney(DepositMoneyRequest request) {
        log.info("Depositing into account: {}", request.accountId());

        DepositMoneyCommand cmd = mapper.toDepositMoneyCommand(request);
        commandGateway.sendAndWait(cmd);

        AccountEntity account = findAccount(request.accountId().toString());

        return DepositMoneyResponse.builder()
                .accountId(request.accountId())
                .transactionId(cmd.transactionId().getValue())
                .depositedAmount(cmd.amount().amount())
                .currency(cmd.amount().currency().getCurrencyCode())
                .newBalance(account.getBalance())
                .message("Deposit completed successfully")
                .build();
    }

    @Override
    public WithdrawMoneyResponse withdrawMoney(WithdrawMoneyRequest request) {
        log.info("Withdrawing from account: {}", request.accountId());

        WithdrawMoneyCommand cmd = mapper.toWithdrawMoneyCommand(request);
        commandGateway.sendAndWait(cmd);

        AccountEntity account = findAccount(request.accountId().toString());

        return WithdrawMoneyResponse.builder()
                .accountId(request.accountId())
                .transactionId(cmd.transactionId().getValue())
                .withdrawnAmount(cmd.amount().amount())
                .currency(cmd.amount().currency().getCurrencyCode())
                .newBalance(account.getBalance())
                .message("Withdrawal completed successfully")
                .build();
    }

    @Override
    public FreezeAccountResponse freezeAccount(FreezeAccountRequest request) {
        log.info("Freezing account: {}", request.accountId());

        FreezeAccountCommand cmd = mapper.toFreezeAccountCommand(request);
        commandGateway.sendAndWait(cmd);

        return FreezeAccountResponse.builder()
                .accountId(request.accountId())
                .accountStatus("FROZEN")
                .reason(request.remark())
                .message("Account frozen successfully")
                .build();
    }

    private AccountEntity findAccount(String accountId) {
        return accountRepository.findById(java.util.UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));
    }
}

