package com.see.visal.account_service.application.dto.freeze;

import lombok.Builder;

import java.util.UUID;

/**
 * Response DTO after freezing an account.
 */
@Builder
public record FreezeAccountResponse(
        UUID accountId,
        String accountStatus,
        String reason,
        String message
) {
}

