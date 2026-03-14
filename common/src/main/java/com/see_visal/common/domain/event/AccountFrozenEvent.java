package com.see_visal.common.domain.event;

import com.see_visal.common.domain.valueoject.AccountStatus;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record AccountFrozenEvent(
        AccountId accountId,
        CustomerId customerId,
        AccountStatus previousStatus,
        AccountStatus newStatus,
        String reason,
        String requestedBy,
        ZonedDateTime createdAt
) {
}
