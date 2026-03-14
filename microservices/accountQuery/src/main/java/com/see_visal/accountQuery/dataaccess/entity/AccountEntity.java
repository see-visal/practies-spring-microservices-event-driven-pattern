package com.see_visal.accountQuery.dataaccess.entity;


import com.see_visal.common.domain.valueoject.AccountStatus;
import com.see_visal.common.domain.valueoject.AccountTypeCode;
import com.see_visal.common.domain.valueoject.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity  {

    @Id
    private UUID accountId;
    private UUID customerId;
    private UUID branchId;

    private String accountNumber;
    private String accountHolder;

    private AccountTypeCode accountTypeCode;

    private AccountStatus status;

    private BigDecimal balance;
    private Currency currency;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    private AccountTypeEntity accountType;


}
