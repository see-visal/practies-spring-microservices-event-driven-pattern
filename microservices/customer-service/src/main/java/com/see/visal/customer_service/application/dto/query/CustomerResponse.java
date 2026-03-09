package com.see.visal.customer_service.application.dto.query;

import com.see.visal.customer_service.domain.valueobject.*;
import com.see_visal.common.domain.valueoject.CustomerSegmentId;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(
        UUID customerId,
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
        String phoneNumber,
        @NotNull
        CustomerSegmentResponse customerSegment

) {
}
