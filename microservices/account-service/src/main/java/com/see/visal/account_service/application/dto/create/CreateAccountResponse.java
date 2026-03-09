package com.see.visal.account_service.application.dto.create;

import lombok.Builder;

import java.util.UUID;

/**
 * Response DTO after creating an account
 */
@Builder
public record CreateAccountResponse(
        UUID accountId,
        String accountNumber,
        String message
) {
}

