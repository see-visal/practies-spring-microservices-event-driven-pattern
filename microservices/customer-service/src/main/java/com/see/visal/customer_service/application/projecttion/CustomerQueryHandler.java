package com.see.visal.customer_service.application.projecttion;


import com.see.visal.customer_service.application.dto.query.CustomPageResponse;
import com.see.visal.customer_service.application.mapper.CustomerApplicationMapper;
import com.see.visal.customer_service.data.entity.CustomerEntity;
import com.see.visal.customer_service.data.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerQueryHandler {

    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper customerApplicationMapper;

    @QueryHandler
    public CustomPageResponse handle(GetCustomerQuery getCustomerQuery) {

        Pageable pageable = PageRequest.of(
                getCustomerQuery.pageNumber(),
                getCustomerQuery.pageSize(),
                Sort.by(Sort.Direction.DESC, "dob")
        );

        // Fetch data from the database using the repository
        Page<CustomerEntity> customerEntityPage = customerRepository.findAll(pageable);

        return customerApplicationMapper.toCustomPageResponse(customerEntityPage);
    }
}