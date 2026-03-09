package com.see.visal.customer_service.application.dto.create;

import com.see.visal.customer_service.domain.valueobject.*;
import com.see_visal.common.domain.valueoject.CustomerSegmentId;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotNull
        CustomerName customerName,
        @NotNull
        CustomerEmail customerEmail,
        @NotNull
        LocalDate dob,
        @NotNull
        CustomerGender customerGender,
        @NotNull
        Kyc kyc,
        @NotNull
        Address address,
        @NotNull
        Contact contact,
        @NotNull
        CustomerSegmentId customerSegmentId,
        @NotNull
        String phoneNumber




) {
}
