package com.see_visal.accountQuery.domain.entity;

import com.google.type.Money;
import com.see_visal.accountQuery.dataaccess.entity.AccountTypeEntity;
import com.see_visal.common.domain.valueoject.AccountStatus;
import com.see_visal.common.domain.valueoject.AccountTypeCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Account {

    private UUID accountId;
    private UUID customerId;
    private UUID branchId;

    private String accountNumber;
    private String accountHolder;

    private AccountTypeCode accountTypeCode;

    private AccountStatus status;

    private Money money;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String createdBy;
    private String updatedBy;


}
