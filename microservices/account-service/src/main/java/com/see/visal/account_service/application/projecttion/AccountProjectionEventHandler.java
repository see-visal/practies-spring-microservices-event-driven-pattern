package com.see.visal.account_service.application.projecttion;

import com.see.visal.account_service.data.entity.AccountEntity;
import com.see.visal.account_service.data.entity.AccountTypeEntity;
import com.see.visal.account_service.data.entity.BranchEntity;
import com.see.visal.account_service.data.repository.AccountRepository;
import com.see.visal.account_service.data.repository.AccountTypeRepository;
import com.see.visal.account_service.data.repository.BranchRepository;
import com.see.visal.account_service.domain.event.AccountCreatedEvent;
import com.see.visal.account_service.domain.event.AccountFrozenEvent;
import com.see.visal.account_service.domain.event.MoneyDepositedEvent;
import com.see.visal.account_service.domain.event.MoneyWithdrawnEvent;
import com.see.visal.account_service.domain.valueobject.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("account-group")
@Transactional
public class AccountProjectionEventHandler {

    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
    private final AccountTypeRepository accountTypeRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("Projecting AccountCreatedEvent to database: {}", event);

        // SKIP customer lookup - customer is in customer-service database, not here
        log.info("Skipping customer lookup - customer data is in customer-service");

        branchRepository.findById(event.branchId().getValue())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Branch not found with ID: " + event.branchId().getValue()));

        AccountTypeEntity accountType = accountTypeRepository.findByAccountTypeCode(event.accountTypeCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account Type not found with code: " + event.accountTypeCode()));

        AccountEntity accountEntity = AccountEntity.builder()
                .accountId(event.accountId().getValue())
                .accountNumber(event.accountNumber())
                .accountHolderName(event.accountHolderName())
                .customerId(event.customerId().getValue())
                .accountTypeId(accountType.getAccountTypeId())
                .accountTypeCode(event.accountTypeCode().name())
                .branchId(event.branchId().getValue())
                .balance(event.initialBalance().amount())
                .availableBalance(event.initialBalance().amount())
                .blockedAmount(BigDecimal.ZERO)
                .minimumBalance(BigDecimal.ZERO)
                .currency(event.initialBalance().currency().getCurrencyCode())
                .accountStatus(AccountStatus.ACTIVE)
                .openedDate(LocalDate.now())
                .lastTransactionDate(LocalDate.now())
                .createdAt(event.createdAt())
                .createdBy(event.createdBy())
                .isJointAccount(false)
                .allowOverdraft(false)
                .allowDebit(true)
                .allowCredit(true)
                .isDormant(false)
                .isBlocked(false)
                .dailyTransactionCount(0)
                .isTaxable(false)
                .build();

        accountRepository.save(accountEntity);
        log.info("Successfully created account entity: {}", accountEntity.getAccountNumber());
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) {
        log.info("Projecting MoneyDepositedEvent to database: {}", event);

        AccountEntity accountEntity = accountRepository.findById(event.accountId().getValue())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with ID: " + event.accountId().getValue()));

        BigDecimal newBalance = accountEntity.getBalance().add(event.amount().amount());
        accountEntity.setBalance(newBalance);
        accountEntity.setAvailableBalance(newBalance);
        accountEntity.setLastTransactionDate(event.createdAt().toLocalDate());

        accountRepository.save(accountEntity);
        log.info("Successfully updated account balance after deposit: {}", accountEntity.getAccountNumber());
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event) {
        log.info("Projecting MoneyWithdrawnEvent to database: {}", event);

        AccountEntity accountEntity = accountRepository.findById(event.accountId().getValue())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with ID: " + event.accountId().getValue()));

        BigDecimal newBalance = accountEntity.getBalance().subtract(event.amount().amount());
        accountEntity.setBalance(newBalance);
        accountEntity.setAvailableBalance(newBalance);
        accountEntity.setLastTransactionDate(event.createdAt().toLocalDate());

        accountRepository.save(accountEntity);
        log.info("Successfully updated account balance after withdrawal: {}", accountEntity.getAccountNumber());
    }

    @EventHandler
    public void on(AccountFrozenEvent event) {
        log.info("Projecting AccountFrozenEvent to database: {}", event);

        AccountEntity accountEntity = accountRepository.findById(event.accountId().getValue())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with ID: " + event.accountId().getValue()));

        accountEntity.setAccountStatus(event.newStatus());
        accountEntity.setStatusReason(event.reason());
        accountEntity.setIsBlocked(true);
        accountEntity.setBlockedDate(event.createdAt().toLocalDate());
        accountEntity.setBlockedReason(event.reason());
        accountEntity.setBlockedBy(event.requestedBy());

        accountRepository.save(accountEntity);
        log.info("Successfully frozen account: {}", accountEntity.getAccountNumber());
    }
}
