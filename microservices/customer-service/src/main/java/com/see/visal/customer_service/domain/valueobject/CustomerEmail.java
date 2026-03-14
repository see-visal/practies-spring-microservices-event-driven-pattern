package com.see.visal.customer_service.domain.valueobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerEmail {
    private String primaryEmail;
    private String secondaryEmail;
}
