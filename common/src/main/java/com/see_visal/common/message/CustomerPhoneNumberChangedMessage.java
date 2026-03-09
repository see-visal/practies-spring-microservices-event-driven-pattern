package com.see_visal.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Shared message for CustomerPhoneNumberChanged event
 * Used for cross-service communication between customer-service and other services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPhoneNumberChangedMessage {
    private UUID customerId;
    private String oldPhoneNumber;
    private String newPhoneNumber;
}

