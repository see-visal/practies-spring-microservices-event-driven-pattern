package com.see.visal.account_service.application.mapper;

import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.domain.command.CreateAccountCommand;
import com.see.visal.account_service.domain.command.DepositMoneyCommand;
import com.see.visal.account_service.domain.command.FreezeAccountCommand;
import com.see.visal.account_service.domain.command.WithdrawMoneyCommand;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.BranchId;
import com.see_visal.common.domain.valueoject.CustomerId;
import com.see_visal.common.domain.valueoject.TransactionId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AccountApplicationMapper {

    private static final DateTimeFormatter ACCOUNT_NUMBER_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public CreateAccountCommand toCreateAccountCommand(CreateAccountRequest request) {
        return CreateAccountCommand.builder()
                .accountId(new AccountId(UUID.randomUUID()))
                .customerId(new CustomerId(request.customerId()))
                .accountNumber(generateAccountNumber())
                .accountHolder(request.accountHolder())
                .accountTypeCode(request.accountTypeCode())
                .branchId(new BranchId(request.branchId()))
                .initialBalance(request.initialBalance())
                .build();
    }

    public DepositMoneyCommand toDepositMoneyCommand(DepositMoneyRequest request) {
        return DepositMoneyCommand.builder()
                .accountId(new AccountId(request.accountId()))
                .transactionId(new TransactionId(UUID.randomUUID()))
                .amount(request.amount())
                .remark(request.remark())
                .build();
    }

    public WithdrawMoneyCommand toWithdrawMoneyCommand(WithdrawMoneyRequest request) {
        return WithdrawMoneyCommand.builder()
                .accountId(new AccountId(request.accountId()))
                .transactionId(new TransactionId(UUID.randomUUID()))
                .amount(request.amount())
                .remark(request.remark())
                .build();
    }

    public FreezeAccountCommand toFreezeAccountCommand(FreezeAccountRequest request) {
        return FreezeAccountCommand.builder()
                .accountId(new AccountId(request.accountId()))
                .remark(request.remark())
                .requestedBy(request.requestedBy() != null ? request.requestedBy() : "SYSTEM")
                .build();
    }

    private String generateAccountNumber() {
        String timestamp = LocalDateTime.now().format(ACCOUNT_NUMBER_FORMATTER);
        int suffix = (int) (Math.random() * 9000) + 1000;
        return "ACC-" + timestamp + "-" + suffix;
    }
}
