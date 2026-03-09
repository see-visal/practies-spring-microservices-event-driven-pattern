package com.see.visal.account_service.application.dto.freeze;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for freezing an account.
 */
public record FreezeAccountRequest(
        @NotNull(message = "Account ID cannot be null")
        UUID accountId,

        String remark,

        String requestedBy
) {
}

