package com.see.visal.account_service.domain.command;

import com.see_visal.common.domain.valueoject.AccountId;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
public record FreezeAccountCommand(
        @TargetAggregateIdentifier
        AccountId accountId,
        String remark,
        String requestedBy
) {
}
