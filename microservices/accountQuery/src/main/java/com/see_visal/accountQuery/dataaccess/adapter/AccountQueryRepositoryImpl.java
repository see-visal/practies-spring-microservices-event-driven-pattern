package com.see_visal.accountQuery.dataaccess.adapter;

import com.see_visal.accountQuery.application.ports.output.repository.AccountQueryRepository;
import com.see_visal.accountQuery.dataaccess.entity.AccountEntity;
import com.see_visal.accountQuery.dataaccess.mapper.AccountDataAccessMapper;
import com.see_visal.accountQuery.dataaccess.repostitory.AccountQueryReactiveRepository;
import com.see_visal.accountQuery.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
// This adapter implement with Spring Data Reactive(R2DBC)
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final AccountQueryReactiveRepository accountQueryReactiveRepository;
    private final AccountDataAccessMapper accountDataAccessMapper;

    @Override
    public Mono<Account> save(Account account) {

        AccountEntity accountEntity = accountDataAccessMapper
                .accountToAccountEntity(account);

        return accountQueryReactiveRepository
                .save(accountEntity)
                .map(accountDataAccessMapper::accountEntityToAccount);
    }

    @Override
    public Mono<Account> findById(UUID accountId) {
        return accountQueryReactiveRepository
                .findById(accountId)
                .map(accountDataAccessMapper::accountEntityToAccount);
    }
}
