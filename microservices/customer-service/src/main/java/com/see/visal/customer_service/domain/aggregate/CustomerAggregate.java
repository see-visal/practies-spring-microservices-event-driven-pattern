package com.see.visal.customer_service.domain.aggregate;

import com.see.visal.customer_service.domain.commend.ChangePhoneNumberCommand;
import com.see.visal.customer_service.domain.commend.CreateCustomerCommand;
import com.see.visal.customer_service.domain.event.CustomerCreatedEvent;
import com.see.visal.customer_service.domain.event.CustomerPhoneNumberChangedEvent;
import com.see.visal.customer_service.domain.valueobject.*;
import com.see_visal.common.domain.valueoject.CustomerId;
import com.see_visal.common.domain.valueoject.CustomerSegmentId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;


@Aggregate(snapshotTriggerDefinition = "customerSnapshotTriggerDefinition")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Slf4j
public class CustomerAggregate {

    @AggregateIdentifier
    private CustomerId customerId;

    private CustomerEmail customerEmail;
    private CustomerName customerName;
    private  String phoneNumber;
    private CustomerGender customerGender;
    private LocalDate dob;
    private Kyc kyc;
    private Address address;
    private Contact contact;
    private CustomerSegmentId customerSegmentId;

 // use this field to store failure messages if any validation fails during command handling
    private List<String > failureMessages;


    // Domain Logic for creating Customer
    // @CommandHandler for handling logic of CreateCustomerCommand
    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand){
       // Perform domain logic for creating a customer
       // Validate email
        validateEmail(createCustomerCommand.customerEmail());
       // Validate phone number
        validatePhoneNumber(createCustomerCommand.phoneNumber());

     // Publish events to CustomerCreatedEvent
    // for this can do 3 ways by builder and by constructor or using MapStruct
    CustomerCreatedEvent customerCreatedEvent = CustomerCreatedEvent
            .builder()
            .customerId(createCustomerCommand.customerId())
            .customerName(createCustomerCommand.customerName())
            .customerEmail(createCustomerCommand.customerEmail())
            .customerGender(createCustomerCommand.customerGender())
            .dob(createCustomerCommand.dob())
            .kyc(createCustomerCommand.kyc())
            .address(createCustomerCommand.address())
            .phoneNumber(createCustomerCommand.phoneNumber())
            .contact(createCustomerCommand.contact())
            .customerSegmentId(createCustomerCommand.customerSegmentId())

            .build();

    // Apply the event to the aggregate for checking the state of the aggregate
    AggregateLifecycle.apply(customerCreatedEvent);

    }


    // Domain Validation Methods
    private void validateEmail(CustomerEmail customerEmail) {
        if (customerEmail == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Customer email cannot be null");
        }
        String primaryEmail = customerEmail.getPrimaryEmail();
        if (primaryEmail == null || primaryEmail.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Primary email cannot be null or empty");
        }

    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Phone number cannot be null or empty");
        }

    }






    @CommandHandler
    public CustomerId handle(ChangePhoneNumberCommand changePhoneNumberCommand){
         log.info("Handling ChangePhoneNumberCommand for customerId: {}", changePhoneNumberCommand.customerId());

        // Validate the new phone number
        validatePhoneNumber(changePhoneNumberCommand.phoneNumber());

        CustomerPhoneNumberChangedEvent customerPhoneNumberChangedEvent = CustomerPhoneNumberChangedEvent
                .builder()
                .customerId(changePhoneNumberCommand.customerId())
                .phoneNumber(changePhoneNumberCommand.phoneNumber())
                .build();
        AggregateLifecycle.apply(customerPhoneNumberChangedEvent);
        return changePhoneNumberCommand.customerId();
    }




    // @EventSourcingHandler for handling the state of the aggregate then save to the database
    @EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent){
        this.customerId = customerCreatedEvent.customerId();
        this.customerName = customerCreatedEvent.customerName();
        this.customerEmail = customerCreatedEvent.customerEmail();
        this.customerGender = customerCreatedEvent.customerGender();
        this.dob = customerCreatedEvent.dob();
        this.kyc = customerCreatedEvent.kyc();
        this.address = customerCreatedEvent.address();
        this.contact = customerCreatedEvent.contact();
        this.customerSegmentId = customerCreatedEvent.customerSegmentId();
        this.phoneNumber = customerCreatedEvent.phoneNumber();

    }

    @EventSourcingHandler
    public void  on (CustomerPhoneNumberChangedEvent customerPhoneNumberChangedEvent){
        log.info("Applying CustomerPhoneNumberChangedEvent for customerId: {}", customerPhoneNumberChangedEvent.customerId());
        this.customerId = customerPhoneNumberChangedEvent.customerId();
        this.phoneNumber = customerPhoneNumberChangedEvent.phoneNumber();
    }



}
