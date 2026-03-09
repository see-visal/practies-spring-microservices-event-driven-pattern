package com.see.visal.account_service.domain.Aggregate;

import com.see.visal.account_service.domain.command.CreateAccountCommand;
import com.see.visal.account_service.domain.command.DepositMoneyCommand;
import com.see.visal.account_service.domain.command.FreezeAccountCommand;
import com.see.visal.account_service.domain.command.WithdrawMoneyCommand;
import com.see.visal.account_service.domain.event.AccountCreatedEvent;
import com.see.visal.account_service.domain.event.AccountFrozenEvent;
import com.see.visal.account_service.domain.event.MoneyDepositedEvent;
import com.see.visal.account_service.domain.event.MoneyWithdrawnEvent;
import com.see.visal.account_service.domain.valueobject.AccountStatus;
import com.see.visal.account_service.domain.valueobject.AccountTypeCode;
import com.see.visal.account_service.domain.valueobject.Money;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.BranchId;
import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Aggregate(snapshotTriggerDefinition = "accountSnapshotTriggerDefinition")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private AccountId accountId;
    private String accountNumber;
    private String accountHolderName;
    private CustomerId customerId;
    private AccountTypeCode accountTypeCode;
    private BranchId branchId;
    private Money balance;
    private AccountStatus accountStatus;
    private ZonedDateTime createdAt;
    private String createdBy;

    // ── Command Handlers ──────────────────────────────────────────────────────

    @CommandHandler
    public AccountAggregate(CreateAccountCommand cmd) {
        log.info("Handling CreateAccountCommand for customer: {}", cmd.customerId());

        validateAccountHolder(cmd.accountHolder());
        validateInitialBalance(cmd.initialBalance());
        validateCustomerId(cmd.customerId());
        validateAccountNumber(cmd.accountNumber());

        AggregateLifecycle.apply(AccountCreatedEvent.builder()
                .accountId(cmd.accountId())
                .accountNumber(cmd.accountNumber())
                .accountHolderName(cmd.accountHolder())
                .customerId(cmd.customerId())
                .accountTypeCode(cmd.accountTypeCode())
                .branchId(cmd.branchId())
                .initialBalance(cmd.initialBalance())
                .createdAt(ZonedDateTime.now())
                .createdBy("SYSTEM")
                .build());
    }

    @CommandHandler
    public void handle(DepositMoneyCommand cmd) {
        log.info("Handling DepositMoneyCommand for account: {}", cmd.accountId());

        validateAccountActive();
        validatePositiveAmount(cmd.amount());
        validateCurrencyMatch(cmd.amount());

        AggregateLifecycle.apply(MoneyDepositedEvent.builder()
                .accountId(cmd.accountId())
                .customerId(customerId)
                .transactionId(cmd.transactionId())
                .amount(cmd.amount())
                .newBalance(balance.add(cmd.amount()))
                .remark(cmd.remark())
                .createdAt(ZonedDateTime.now())
                .build());
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand cmd) {
        log.info("Handling WithdrawMoneyCommand for account: {}", cmd.accountId());

        validateAccountActive();
        validatePositiveAmount(cmd.amount());
        validateCurrencyMatch(cmd.amount());
        validateSufficientBalance(cmd.amount());

        AggregateLifecycle.apply(MoneyWithdrawnEvent.builder()
                .accountId(cmd.accountId())
                .customerId(customerId)
                .transactionId(cmd.transactionId())
                .amount(cmd.amount())
                .newBalance(balance.subtract(cmd.amount()))
                .remark(cmd.remark())
                .createdAt(ZonedDateTime.now())
                .build());
    }

    @CommandHandler
    public void handle(FreezeAccountCommand cmd) {
        log.info("Handling FreezeAccountCommand for account: {}", cmd.accountId());

        if (accountStatus == AccountStatus.FROZEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account is already frozen");
        }
        if (accountStatus == AccountStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot freeze a closed account");
        }

        AggregateLifecycle.apply(AccountFrozenEvent.builder()
                .accountId(accountId)
                .customerId(customerId)
                .previousStatus(accountStatus)
                .newStatus(AccountStatus.FROZEN)
                .reason(cmd.remark())
                .requestedBy(cmd.requestedBy())
                .createdAt(ZonedDateTime.now())
                .build());
    }

    // ── Event Sourcing Handlers ───────────────────────────────────────────────

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId         = event.accountId();
        this.accountNumber     = event.accountNumber();
        this.accountHolderName = event.accountHolderName();
        this.customerId        = event.customerId();
        this.accountTypeCode   = event.accountTypeCode();
        this.branchId          = event.branchId();
        this.balance           = event.initialBalance();
        this.accountStatus     = AccountStatus.ACTIVE;
        this.createdAt         = event.createdAt();
        this.createdBy         = event.createdBy();
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent event) {
        this.balance = event.newBalance();
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event) {
        this.balance = event.newBalance();
    }

    @EventSourcingHandler
    public void on(AccountFrozenEvent event) {
        this.accountStatus = event.newStatus();
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private void validateAccountHolder(String accountHolder) {
        if (accountHolder == null || accountHolder.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account holder name cannot be blank");
        }
    }

    private void validateInitialBalance(Money initialBalance) {
        if (initialBalance == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial balance cannot be null");
        }
        if (initialBalance.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial balance cannot be negative");
        }
    }

    private void validateCustomerId(CustomerId customerId) {
        if (customerId == null || customerId.getValue() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID cannot be null");
        }
    }

    private void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number cannot be blank");
        }
    }

    private void validateAccountActive() {
        if (accountStatus != AccountStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account is not active. Current status: " + accountStatus);
        }
    }

    private void validatePositiveAmount(Money money) {
        if (money == null || money.isNegative() || money.isZero()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction amount must be greater than zero");
        }
    }

    private void validateCurrencyMatch(Money money) {
        if (!balance.currency().code().equals(money.currency().code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Currency mismatch: account is " + balance.currency().code()
                            + " but transaction is " + money.currency().code());
        }
    }

    private void validateSufficientBalance(Money amount) {
        if (!balance.isGreaterThanOrEqualTo(amount)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Insufficient balance. Available: " + balance + ", Required: " + amount);
        }
    }
}
