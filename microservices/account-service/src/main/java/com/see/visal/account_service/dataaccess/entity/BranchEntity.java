package com.see.visal.account_service.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BranchEntity {

    @Id
    private UUID branchId;

    @Column(nullable = false, length = 255)
    private String branchName;

    @Column(nullable = false)
    private Boolean isOpening;
}
