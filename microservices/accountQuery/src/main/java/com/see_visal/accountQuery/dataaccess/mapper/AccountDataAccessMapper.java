package com.see_visal.accountQuery.dataaccess.mapper;

import com.see_visal.accountQuery.dataaccess.entity.AccountEntity;
import com.see_visal.accountQuery.domain.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountDataAccessMapper {


    AccountEntity accountToAccountEntity(Account account);

    Account accountEntityToAccount(AccountEntity accountEntity);

}
