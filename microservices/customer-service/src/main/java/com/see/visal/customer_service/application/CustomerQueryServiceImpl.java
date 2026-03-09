package com.see.visal.customer_service.application;


import com.see.visal.customer_service.application.dto.query.CustomPageResponse;
import com.see.visal.customer_service.application.projecttion.GetCustomerQuery;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService{

    private final QueryGateway queryGateway;
    private final EventStore eventStore;


    @Override
    public List<?> getCustomerHistory(UUID customerId) {
        return eventStore.readEvents(customerId.toString())
                .asStream()
                .map(Message::getPayload)
                .toList();
    }

    @Override
    public CustomPageResponse getAllCustomers(int pageNumber, int pageSize){
        GetCustomerQuery getCustomerQuery = new GetCustomerQuery(pageNumber, pageSize);
        return queryGateway
                .query(getCustomerQuery, ResponseTypes.instanceOf(CustomPageResponse.class))
                .join();
    }
}