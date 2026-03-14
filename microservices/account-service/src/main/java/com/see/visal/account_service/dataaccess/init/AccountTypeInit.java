package com.see.visal.account_service.data.init;

import com.see.visal.account_service.data.entity.AccountTypeEntity;
import com.see.visal.account_service.data.repository.AccountTypeRepository;
import com.see_visal.common.domain.valueoject.AccountTypeCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTypeInit implements ApplicationListener<ApplicationReadyEvent> {

    private final AccountTypeRepository accountTypeRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (accountTypeRepository.count() == 0) {
            log.info("Initializing Account Types...");

            List<AccountTypeEntity> accountTypes = new ArrayList<>();
            ZonedDateTime now = ZonedDateTime.now();

            // Savings Account
            AccountTypeEntity savings = new AccountTypeEntity();
            savings.setAccountTypeId(UUID.randomUUID());
            savings.setAccountTypeCode(AccountTypeCode.SAVINGS);
            savings.setAccountTypeName("Savings Account");
            savings.setDescription("Standard savings account with interest");
            savings.setInterestRate(BigDecimal.valueOf(2.5));
            savings.setMinimumBalance(BigDecimal.valueOf(1000));
            savings.setIsActive(true);
            savings.setCreatedAt(now);
            savings.setCreatedBy("SYSTEM");
            accountTypes.add(savings);

            // Checking Account
            AccountTypeEntity checking = new AccountTypeEntity();
            checking.setAccountTypeId(UUID.randomUUID());
            checking.setAccountTypeCode(AccountTypeCode.CHECKING);
            checking.setAccountTypeName("Checking Account");
            checking.setDescription("Everyday transaction account");
            checking.setInterestRate(BigDecimal.valueOf(0.5));
            checking.setMinimumBalance(BigDecimal.valueOf(500));
            checking.setIsActive(true);
            checking.setCreatedAt(now);
            checking.setCreatedBy("SYSTEM");
            accountTypes.add(checking);

            // Business Account
            AccountTypeEntity business = new AccountTypeEntity();
            business.setAccountTypeId(UUID.randomUUID());
            business.setAccountTypeCode(AccountTypeCode.BUSINESS);
            business.setAccountTypeName("Business Account");
            business.setDescription("Account for business transactions");
            business.setInterestRate(BigDecimal.valueOf(1.5));
            business.setMinimumBalance(BigDecimal.valueOf(5000));
            business.setIsActive(true);
            business.setCreatedAt(now);
            business.setCreatedBy("SYSTEM");
            accountTypes.add(business);

            // Payroll Account
            AccountTypeEntity payroll = new AccountTypeEntity();
            payroll.setAccountTypeId(UUID.randomUUID());
            payroll.setAccountTypeCode(AccountTypeCode.PAYROLL);
            payroll.setAccountTypeName("Payroll Account");
            payroll.setDescription("Account for salary and payroll transactions");
            payroll.setInterestRate(BigDecimal.valueOf(0.25));
            payroll.setMinimumBalance(BigDecimal.valueOf(100));
            payroll.setIsActive(true);
            payroll.setCreatedAt(now);
            payroll.setCreatedBy("SYSTEM");
            accountTypes.add(payroll);

            accountTypeRepository.saveAll(accountTypes);
            log.info("Initialized {} account types", accountTypes.size());
        }
    }
}

