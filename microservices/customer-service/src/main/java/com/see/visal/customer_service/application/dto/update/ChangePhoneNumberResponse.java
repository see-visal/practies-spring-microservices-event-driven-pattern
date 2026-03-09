package com.see.visal.customer_service.application.dto.update;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChangePhoneNumberResponse(
        @NotNull
        UUID customerId,
        @NotNull
        String message,
        @NotNull
        String phoneNumber
) {
}
