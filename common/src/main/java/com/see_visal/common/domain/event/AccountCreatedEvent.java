package com.see_visal.common.domain.event;

import com.see_visal.common.domain.valueoject.Money;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.AccountTypeCode;
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
