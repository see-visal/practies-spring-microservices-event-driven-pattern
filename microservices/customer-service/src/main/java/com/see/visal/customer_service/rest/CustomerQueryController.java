package com.see.visal.customer_service.rest;

import com.see.visal.customer_service.application.CustomerQueryService;
import com.see.visal.customer_service.application.dto.query.CustomPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerQueryController {

    private final CustomerQueryService customerQueryService;

    @GetMapping("/{customerId}/history")
    public List<?> getCustomerHistory(@PathVariable UUID customerId) {
        return customerQueryService.getCustomerHistory(customerId);
    }

    @GetMapping
    public CustomPageResponse getAllCustomers(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "5", required = false) int pageSize
    ) {
        return customerQueryService.getAllCustomers(pageNumber, pageSize);
    }
}

