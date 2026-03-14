package com.see.visal.account_service.domain.command;

import com.see_visal.common.domain.valueoject.AccountTypeCode;
//import com.see.visal.account_service.domain.valueobject.BranchId;
import com.see_visal.common.domain.valueoject.Money;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.BranchId;
import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
public record CreateAccountCommand(
        @TargetAggregateIdentifier
        AccountId accountId,
        CustomerId customerId,
        String accountNumber,
        String accountHolder,
        AccountTypeCode accountTypeCode,
        BranchId branchId,
        Money initialBalance
) {
}
