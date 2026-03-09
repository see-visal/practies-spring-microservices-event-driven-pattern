package com.see.visal.customer_service.rest;

import com.see.visal.customer_service.application.CustomerService;
import com.see.visal.customer_service.application.dto.create.CreateCustomerRequest;
import com.see.visal.customer_service.application.dto.create.CreateCustomerResponse;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberRequest;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateCustomerResponse createCustomer(@Validated @RequestBody CreateCustomerRequest createCustomerRequest) {
         log.info("Create customer: {}", createCustomerRequest);
         return  customerService.createCustomer(createCustomerRequest);
    }

    @PutMapping("/{customerId}/phone-number")
    public ChangePhoneNumberResponse changePhoneNumber(@PathVariable UUID customerId, @Validated @RequestBody ChangePhoneNumberRequest changePhoneNumberRequest) {
        log.info("Change phone number for customer with id: {}, new phone number: {}", customerId, changePhoneNumberRequest.phoneNumber());
        return customerService.changePhoneNumber(customerId, changePhoneNumberRequest);


    }


}

