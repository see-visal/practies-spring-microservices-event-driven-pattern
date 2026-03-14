package com.see_visal.accountQuery.dataaccess.adapter;


import com.see_visal.accountQuery.application.ports.output.repository.AccountQueryRepository;
import com.see_visal.accountQuery.domain.entity.Account;
import reactor.core.publisher.Mono;

import java.util.UUID;
// This adapter implement with JPA
public class AccountQueryJpaRepositoryAdapter implements AccountQueryRepository {

    @Override
    public Mono<Account> save(Account account) {
        return null;
    }

    @Override
    public Mono<Account> findById(UUID accountId) {
        return null;
    }

}
