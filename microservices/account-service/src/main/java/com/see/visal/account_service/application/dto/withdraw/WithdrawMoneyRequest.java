package com.see.visal.account_service.application.dto.withdraw;

import com.see_visal.common.domain.valueoject.Money;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for withdrawing money from an account.
 */
public record WithdrawMoneyRequest(
        @NotNull(message = "Account ID cannot be null")
        UUID accountId,

        @NotNull(message = "Amount cannot be null")
        Money amount,

        String remark
) {
}

