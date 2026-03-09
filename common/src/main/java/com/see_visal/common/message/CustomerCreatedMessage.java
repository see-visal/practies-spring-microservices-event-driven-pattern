package com.see_visal.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Shared message for CustomerCreated event
 * Used for cross-service communication between customer-service and other services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedMessage {
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String phoneNumber;
    private String gender;
}

