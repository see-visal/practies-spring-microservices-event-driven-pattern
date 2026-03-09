package com.see.visal.customer_service.domain.event;


import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.Builder;



@Builder
public record CustomerPhoneNumberChangedEvent(
        CustomerId customerId,
        String phoneNumber


) {
}
