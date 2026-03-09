package com.see.visal.account_service.data.repository;

import com.see.visal.account_service.data.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, UUID> {
   // Optional<BranchEntity> findByBranchName(String branchName);
}

