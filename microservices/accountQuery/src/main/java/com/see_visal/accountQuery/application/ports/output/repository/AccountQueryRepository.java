package com.see_visal.accountQuery.application.ports.output.repository;

import com.see_visal.accountQuery.domain.entity.Account;
import reactor.core.publisher.Mono;

import java.util.UUID;

// Output port for data access technologies
public interface AccountQueryRepository {

    Mono<Account> save(Account account);

    Mono<Account> findById(UUID accountId);
}
