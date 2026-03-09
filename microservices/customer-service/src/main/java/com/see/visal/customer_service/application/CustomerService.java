package com.see.visal.customer_service.application;

import com.see.visal.customer_service.application.dto.create.CreateCustomerRequest;
import com.see.visal.customer_service.application.dto.create.CreateCustomerResponse;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberRequest;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberResponse;

import java.util.UUID;


public interface CustomerService {
    ChangePhoneNumberResponse changePhoneNumber (UUID customerId, ChangePhoneNumberRequest changePhoneNumberRequest);
    CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest);
}
