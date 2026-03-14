package com.see_visal.accountQuery.application.mapper;


import com.see_visal.common.domain.event.AccountCreatedEvent;
import com.see_visal.accountQuery.application.dto.AccountQueryResponse;
import com.see_visal.accountQuery.domain.entity.Account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountAppDataMapper {


    AccountQueryResponse accountToAccountQueryResponse(Account account);


    Account accountCreatedEventToAccount(AccountCreatedEvent accountCreatedEvent);

}
