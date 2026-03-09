package com.see.visal.customer_service.data.entity;

import com.see.visal.customer_service.domain.valueobject.CustomerEmail;
import com.see.visal.customer_service.domain.valueobject.CustomerGender;
import com.see.visal.customer_service.domain.valueobject.CustomerName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "familyName", column = @Column(name = "family_name")),
            @AttributeOverride(name = "givenName",  column = @Column(name = "given_name"))
    })
    private CustomerName customerName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "primaryEmail",   column = @Column(name = "primary_email")),
            @AttributeOverride(name = "secondaryEmail", column = @Column(name = "secondary_email"))
    })
    private CustomerEmail customerEmail;

    private LocalDate dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender")
    private CustomerGender customerGender;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private KycEntity kyc;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private AddressEntity address;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ContactEntity contact;

    @ManyToOne
    @JoinColumn(name = "customer_segment_id")
    private CustomerSegmentEntity customerSegment;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "failure_messages", columnDefinition = "jsonb")
    private List<String> failureMessages;

}
