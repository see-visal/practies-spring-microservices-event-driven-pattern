package com.see_visal.accountQuery.application.dto;

import java.util.UUID;

public record AccountQueryResponse(
        UUID accountId,
        String accountNumber
) {
}
