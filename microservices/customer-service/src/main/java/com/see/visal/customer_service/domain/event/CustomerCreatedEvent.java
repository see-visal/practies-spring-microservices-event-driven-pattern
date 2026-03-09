package com.see.visal.customer_service.domain.event;

import com.see.visal.customer_service.domain.valueobject.*;
import com.see_visal.common.domain.valueoject.CustomerId;
import com.see_visal.common.domain.valueoject.CustomerSegmentId;
import lombok.Builder;


import java.time.LocalDate;
@Builder
public record CustomerCreatedEvent(
        CustomerId customerId,
        CustomerName customerName,
        CustomerEmail customerEmail,
        String phoneNumber,
        CustomerGender customerGender,
        LocalDate dob,
        Kyc kyc,
        Address address,
        Contact contact,
        CustomerSegmentId customerSegmentId
) {
}
