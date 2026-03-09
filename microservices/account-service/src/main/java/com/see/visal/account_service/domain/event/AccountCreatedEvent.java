package com.see.visal.account_service.domain.event;

import com.see.visal.account_service.domain.valueobject.AccountTypeCode;
//import com.see.visal.account_service.domain.valueobject.BranchId;
import com.see.visal.account_service.domain.valueobject.Money;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.BranchId;
import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record AccountCreatedEvent(
        AccountId accountId,
        String accountHolderName,
        String accountNumber,
        CustomerId customerId,
        AccountTypeCode accountTypeCode,
        BranchId branchId,
        Money initialBalance,
        ZonedDateTime createdAt,
        String createdBy


) {
}
