package com.see.visal.customer_service.data.entity;


import com.see.visal.customer_service.domain.valueobject.CustomerSegmentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_segments")
public class CustomerSegmentEntity {

    @Id
    @Column(name = "customer_segment_id")
    private UUID customerSegmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_segment_type")
    private CustomerSegmentType customerSegmentType;

    @OneToMany(mappedBy = "customerSegment", cascade = CascadeType.ALL)
    private List<CustomerEntity> customer;

}
