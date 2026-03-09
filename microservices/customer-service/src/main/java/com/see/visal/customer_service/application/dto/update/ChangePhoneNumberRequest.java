package com.see.visal.customer_service.application.dto.update;

import jakarta.validation.constraints.NotNull;

public record ChangePhoneNumberRequest(
        @NotNull
        String phoneNumber


) {
}
