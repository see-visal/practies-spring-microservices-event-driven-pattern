package com.see.visal.account_service.data.entity;

import com.see.visal.account_service.domain.valueobject.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_account_number", columnList = "account_number"),
        @Index(name = "idx_customer_id",    columnList = "customer_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountEntity {

    @Id
    private UUID accountId;

    @Column(nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 200)
    private String accountHolderName;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID accountTypeId;

    @Column(nullable = false, length = 50)
    private String accountTypeCode;

    @Column(nullable = false)
    private UUID branchId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal availableBalance;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal blockedAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal minimumBalance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus accountStatus;

    @Column
    private String statusReason;

    @Column(nullable = false)
    private LocalDate openedDate;

    @Column
    private LocalDate lastTransactionDate;

    @Column(nullable = false)
    private Boolean isJointAccount;

    @Column(nullable = false)
    private Boolean allowOverdraft;

    @Column(nullable = false)
    private Boolean allowDebit;

    @Column(nullable = false)
    private Boolean allowCredit;

    @Column(nullable = false)
    private Boolean isDormant;

    @Column(nullable = false)
    private Boolean isBlocked;

    @Column
    private LocalDate blockedDate;

    @Column
    private String blockedReason;

    @Column
    private String blockedBy;

    @Column(nullable = false)
    private Integer dailyTransactionCount;

    @Column(nullable = false)
    private Boolean isTaxable;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(length = 100)
    private String createdBy;

    @Column
    private ZonedDateTime updatedAt;

    @Column(length = 100)
    private String updatedBy;


    @Version
    private Long version;
}
