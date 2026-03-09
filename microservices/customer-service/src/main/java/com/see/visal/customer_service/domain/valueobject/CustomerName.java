package com.see.visal.customer_service.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CustomerName {
    private String familyName;
    private String givenName;
}
