package com.see_visal.accountQuery.application.ports.input.service;

import com.see_visal.accountQuery.application.dto.AccountQueryResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountQueryService {

    Mono<AccountQueryResponse> getAccountById(UUID accountId);


}
