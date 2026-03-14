package com.see.visal.account_service.application.dto.deposit;

import com.see_visal.common.domain.valueoject.Money;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DepositMoneyRequest(
        @NotNull(message = "Account ID cannot be null")
        UUID accountId,

        @NotNull(message = "Amount cannot be null")
        Money amount,

        String remark
) {}
