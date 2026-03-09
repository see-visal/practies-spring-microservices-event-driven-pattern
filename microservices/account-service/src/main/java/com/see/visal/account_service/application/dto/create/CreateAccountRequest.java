package com.see.visal.account_service.application.dto.create;

import com.see.visal.account_service.domain.valueobject.AccountTypeCode;
import com.see.visal.account_service.domain.valueobject.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for creating a new account
 */
public record CreateAccountRequest(
        @NotNull(message = "Customer ID cannot be null")
        UUID customerId,

        @NotBlank(message = "Account holder name cannot be blank")
        String accountHolder,

        @NotNull(message = "Account type cannot be null")
        AccountTypeCode accountTypeCode,

        @NotNull(message = "Branch ID cannot be null")
        UUID branchId,

        @NotNull(message = "Initial balance cannot be null")
        Money initialBalance
) {
}

