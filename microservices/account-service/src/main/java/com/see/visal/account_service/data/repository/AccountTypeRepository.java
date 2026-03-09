package com.see.visal.account_service.data.repository;

import com.see.visal.account_service.data.entity.AccountTypeEntity;
import com.see.visal.account_service.domain.valueobject.AccountTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypeEntity, UUID> {
    Optional<AccountTypeEntity> findByAccountTypeCode(AccountTypeCode accountTypeCode);
    Optional<AccountTypeEntity> findByAccountTypeName(String accountTypeName);
}

