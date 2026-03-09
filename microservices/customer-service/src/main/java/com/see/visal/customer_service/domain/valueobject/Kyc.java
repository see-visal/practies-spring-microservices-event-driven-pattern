package com.see.visal.customer_service.domain.valueobject;

import java.util.UUID;

public record Kyc(
        UUID kycId,
        String type,
        String number
) {
}
