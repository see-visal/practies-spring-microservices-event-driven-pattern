package com.see.visal.customer_service.domain.commend;

import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
public record ChangePhoneNumberCommand(
        @TargetAggregateIdentifier
        CustomerId customerId,
        String phoneNumber
) {
}
