package com.see.visal.account_service.application.dto.withdraw;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO after withdrawing money from an account.
 */
@Builder
public record WithdrawMoneyResponse(
        UUID accountId,
        UUID transactionId,
        BigDecimal withdrawnAmount,
        String currency,
        BigDecimal newBalance,
        String message
) {
}

