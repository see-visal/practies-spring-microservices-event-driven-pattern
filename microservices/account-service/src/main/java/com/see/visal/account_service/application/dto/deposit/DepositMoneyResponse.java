package com.see.visal.account_service.application.dto.deposit;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO after depositing money into an account.
 */
@Builder
public record DepositMoneyResponse(
        UUID accountId,
        UUID transactionId,
        BigDecimal depositedAmount,
        String currency,
        BigDecimal newBalance,
        String message
) {
}

