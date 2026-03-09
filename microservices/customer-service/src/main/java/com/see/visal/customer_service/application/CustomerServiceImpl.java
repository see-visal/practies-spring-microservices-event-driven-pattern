package com.see.visal.customer_service.application;

import com.see.visal.customer_service.application.dto.create.CreateCustomerRequest;
import com.see.visal.customer_service.application.dto.create.CreateCustomerResponse;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberRequest;
import com.see.visal.customer_service.application.dto.update.ChangePhoneNumberResponse;
import com.see.visal.customer_service.application.mapper.CustomerApplicationMapper;
import com.see.visal.customer_service.domain.commend.ChangePhoneNumberCommand;
import com.see.visal.customer_service.domain.commend.CreateCustomerCommand;
import com.see_visal.common.domain.valueoject.CustomerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerApplicationMapper customerMapper;
    private final CommandGateway commandGateway;


    @Override
    public ChangePhoneNumberResponse changePhoneNumber(UUID customerId, ChangePhoneNumberRequest changePhoneNumberRequest) {

        // 1. Transfer data from request to command
        ChangePhoneNumberCommand changePhoneNumberCommand = ChangePhoneNumberCommand.builder()
                .customerId(new CustomerId(customerId))
                .phoneNumber(changePhoneNumberRequest.phoneNumber())
                .build();
        log.info("ChangePhoneNumberCommand: {}", changePhoneNumberCommand);

        CustomerId result = commandGateway.sendAndWait(changePhoneNumberCommand);

        return ChangePhoneNumberResponse.builder()
                .customerId(result.getValue())
                .phoneNumber(changePhoneNumberCommand.phoneNumber())
                .message("Phone number changed successfully")
                .build();
    }


    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {

        // 1. Transfer data from request to command
        CreateCustomerCommand createCustomerCommand = customerMapper
                .createCustomerRequestToCreateCustomerCommand(new CustomerId(UUID.randomUUID()) , createCustomerRequest);
        log.info("CreateCustomerCommand: {}", createCustomerCommand);

        // 2. Invoke and handle Axon command gateway
        CustomerId result = commandGateway.sendAndWait(createCustomerCommand);
        log.info("CommandGateway Result: {}", result);

        return CreateCustomerResponse.builder()
                .customerId(createCustomerCommand.customerId().getValue())
                .message("Customer saved successfully")
                .build();
    }
}
