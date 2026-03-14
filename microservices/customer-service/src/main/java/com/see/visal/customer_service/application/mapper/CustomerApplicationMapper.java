package com.see.visal.customer_service.application.mapper;

import com.see.visal.customer_service.application.dto.create.CreateCustomerRequest;
import com.see.visal.customer_service.application.dto.query.CustomPageResponse;
import com.see.visal.customer_service.application.dto.query.CustomerResponse;
import com.see.visal.customer_service.data.entity.AddressEntity;
import com.see.visal.customer_service.data.entity.ContactEntity;
import com.see.visal.customer_service.data.entity.CustomerEntity;
import com.see.visal.customer_service.data.entity.KycEntity;
import com.see.visal.customer_service.domain.commend.CreateCustomerCommand;
import com.see.visal.customer_service.domain.event.CustomerCreatedEvent;
import com.see.visal.customer_service.domain.valueobject.Address;
import com.see.visal.customer_service.domain.valueobject.Contact;
import com.see.visal.customer_service.domain.valueobject.Kyc;
import com.see_visal.common.domain.valueoject.CustomerId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;


import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerApplicationMapper {

    CustomerResponse customerEntityToCustomerResponse(CustomerEntity customerEntity);


    CreateCustomerCommand createCustomerRequestToCreateCustomerCommand
            (CustomerId customerId, CreateCustomerRequest  createCustomerRequest);

    @Mapping(source = "customerId.value" , target = "customerId")
    @Mapping(source = "kyc"     , target = "kyc", qualifiedByName = "kycToKycEntity")
    @Mapping(source = "address" , target = "address", qualifiedByName = "addressToAddressEntity")
    @Mapping(source = "contact" , target = "contact", qualifiedByName = "contactToContactEntity")
    CustomerEntity customerCreateEventToCustomerEntity(CustomerCreatedEvent customerCreatedEvent);

    // Kyc domain value object → KycEntity
    @Named("kycToKycEntity")
    @Mapping(source = "kycId"  , target = "kycId")
    @Mapping(source = "type"   , target = "kycType")
    @Mapping(source = "number" , target = "kycNumber")
    KycEntity kycToKycEntity(Kyc kyc);

    // Address domain value object → AddressEntity
    @Named("addressToAddressEntity")
    @Mapping(source = "addressId" , target = "addressId")
    @Mapping(source = "line"      , target = "line")
    @Mapping(source = "city"      , target = "city")
    @Mapping(source = "country"   , target = "country")
    @Mapping(source = "zipCode"   , target = "zipCode")
    AddressEntity addressToAddressEntity(Address address);

    // Contact domain value object → ContactEntity
    @Named("contactToContactEntity")
    @Mapping(source = "contactId" , target = "contactId")
    @Mapping(source = "type"      , target = "type")
    @Mapping(source = "number"    , target = "number")
    ContactEntity contactToContactEntity(Contact contact);

    default CustomPageResponse toCustomPageResponse(Page<CustomerEntity > customerEntityPage) {

        List<CustomerResponse> content = customerEntityPage
                .map(this::customerEntityToCustomerResponse)
                .toList();

        return CustomPageResponse.builder()
                .content(content)
                .pageNumber(customerEntityPage.getNumber())
                .pageSize(customerEntityPage.getSize())
                .totalElements(customerEntityPage.getTotalElements())
                .totalPages(customerEntityPage.getTotalPages())
                .build();
    }


}
