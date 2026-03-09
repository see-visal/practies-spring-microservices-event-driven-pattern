package com.see.visal.customer_service.application.listener;

import com.see.visal.customer_service.application.mapper.CustomerApplicationMapper;
import com.see.visal.customer_service.data.entity.CustomerEntity;
import com.see.visal.customer_service.data.entity.CustomerSegmentEntity;
import com.see.visal.customer_service.data.repository.CustomerRepository;
import com.see.visal.customer_service.data.repository.CustomerSegmentRepository;
import com.see.visal.customer_service.domain.event.CustomerCreatedEvent;
import com.see.visal.customer_service.domain.event.CustomerPhoneNumberChangedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@ProcessingGroup("customer-group")
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerListener {
    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper customerApplicationMapper;
    private final CustomerSegmentRepository customerSegmentRepository;
    @EventHandler
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        log.info("On CustomerCreatedEvent {}", customerCreatedEvent);

        // Idempotency: skip if already persisted (e.g. during replay)
        UUID customerId = customerCreatedEvent.customerId().getValue();
        if (customerRepository.existsById(customerId)) {
            log.warn("CustomerCreatedEvent already processed for ID: {}. Skipping.", customerId);
            return;
        }

        CustomerEntity customerEntity =
                customerApplicationMapper.customerCreateEventToCustomerEntity(customerCreatedEvent);

        log.info("Mapped CustomerEntity - ID: {}, Name: {}, Email: {}, Phone: {}",
                customerEntity.getCustomerId(),
                customerEntity.getCustomerName(),
                customerEntity.getCustomerEmail(),
                customerEntity.getPhoneNumber());

        // Wire back-references so child FKs point to the parent
        if (customerEntity.getAddress() != null) {
            customerEntity.getAddress().setCustomer(customerEntity);
        }
        if (customerEntity.getContact() != null) {
            customerEntity.getContact().setCustomer(customerEntity);
        }
        if (customerEntity.getKyc() != null) {
            customerEntity.getKyc().setCustomer(customerEntity);
        }


        // Resolve and set the customer segment FK
        if (customerCreatedEvent.customerSegmentId() != null
                && customerCreatedEvent.customerSegmentId().customerSegmentId() != null) {

            CustomerSegmentEntity segment = customerSegmentRepository
                    .findById(customerCreatedEvent.customerSegmentId().customerSegmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Segment Not Found"));

            customerEntity.setCustomerSegment(segment);
        } else {
            log.info("No customer segment ID provided. Customer will be created without segment.");
        }

        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        log.info("Customer saved successfully with ID: {}, Phone: {}",
                savedCustomer.getCustomerId(),
                savedCustomer.getPhoneNumber());

    }


    @EventHandler
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(CustomerPhoneNumberChangedEvent customerPhoneNumberChangedEvent) {
        log.info("on CustomerPhoneNumberChangedEvent: {}", customerPhoneNumberChangedEvent);
        // 1. Find existing customer
        CustomerEntity customerEntity = customerRepository
                .findById(customerPhoneNumberChangedEvent.customerId().getValue() )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Customer not found"
                        )
                );
        // 2. Update phone number in customer entity
        customerEntity.setPhoneNumber(customerPhoneNumberChangedEvent.phoneNumber());

        // 3. Update phone number in contact entity if exists
        if (customerEntity.getContact() != null) {
            customerEntity.getContact().setNumber(customerPhoneNumberChangedEvent.phoneNumber());
            log.info("Updated contact number for customer: {}", customerPhoneNumberChangedEvent.customerId());
        }

        // 4. Save (update)
        customerRepository.save(customerEntity);
        log.info("Phone number updated successfully for customer: {}", customerPhoneNumberChangedEvent.customerId());
    }

}
