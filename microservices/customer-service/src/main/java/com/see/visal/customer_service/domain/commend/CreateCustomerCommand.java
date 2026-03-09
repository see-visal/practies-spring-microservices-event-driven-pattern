package com.see.visal.customer_service.domain.commend;

import com.see.visal.customer_service.domain.valueobject.*;
import com.see_visal.common.domain.valueoject.CustomerId;
import com.see_visal.common.domain.valueoject.CustomerSegmentId;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

public record CreateCustomerCommand(
        @TargetAggregateIdentifier
        CustomerId customerId,
        CustomerName customerName,
        CustomerEmail customerEmail,
        CustomerGender customerGender,
        String phoneNumber,
        LocalDate dob,
        Kyc kyc,
        Address address,
        Contact contact,
        CustomerSegmentId customerSegmentId




) {
}
