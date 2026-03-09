package com.see.visal.account_service.data.entity;

import com.see.visal.account_service.domain.valueobject.AccountTypeCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_types", indexes = {
    @Index(name = "idx_account_type_code", columnList = "account_type_code")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountTypeEntity {

    @Id
    private UUID accountTypeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private AccountTypeCode accountTypeCode;

    @Column(nullable = false, length = 255)
    private String accountTypeName;

    @Column(length = 1000)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(precision = 19, scale = 4)
    private BigDecimal minimumBalance;

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
