package com.see.visal.account_service.domain.command;

import com.see.visal.account_service.domain.valueobject.Money;
import com.see_visal.common.domain.valueoject.TransactionId;
import com.see_visal.common.domain.valueoject.AccountId;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
public record DepositMoneyCommand(
        @TargetAggregateIdentifier
        AccountId accountId,
        TransactionId transactionId,
        Money amount,
        String remark
) {
}
