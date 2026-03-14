package com.see_visal.accountQuery.application;


import com.see_visal.accountQuery.application.dto.AccountQueryResponse;
import com.see_visal.accountQuery.application.mapper.AccountAppDataMapper;
import com.see_visal.accountQuery.application.ports.input.service.AccountQueryService;
import com.see_visal.accountQuery.application.ports.output.repository.AccountQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountAppDataMapper accountAppDataMapper;
    private final AccountQueryRepository accountQueryRepository;

    @Override
    public Mono<AccountQueryResponse> getAccountById(UUID accountId) {
        return accountQueryRepository
                .findById(accountId)
                .map(accountAppDataMapper::accountToAccountQueryResponse);


    }


}
