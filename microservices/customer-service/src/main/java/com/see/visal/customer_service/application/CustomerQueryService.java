package com.see.visal.customer_service.application;

import com.see.visal.customer_service.application.dto.query.CustomPageResponse;

import java.util.List;
import java.util.UUID;


public interface CustomerQueryService {

    List<?> getCustomerHistory(UUID customerId);

    CustomPageResponse getAllCustomers(int pageNumber, int pageSize);

}
