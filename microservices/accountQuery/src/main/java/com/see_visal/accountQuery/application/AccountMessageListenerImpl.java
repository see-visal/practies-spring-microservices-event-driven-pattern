package com.see_visal.accountQuery.application;




import com.see_visal.common.domain.event.AccountCreatedEvent;
import com.see_visal.accountQuery.application.mapper.AccountAppDataMapper;
import com.see_visal.accountQuery.application.ports.input.message.listener.AccountMessageListener;
import com.see_visal.accountQuery.application.ports.output.repository.AccountQueryRepository;
import com.see_visal.accountQuery.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountMessageListenerImpl implements AccountMessageListener {

    private final AccountQueryRepository accountQueryRepository;
    private final AccountAppDataMapper accountAppDataMapper;

    @Override
    public void onAccountCreatedEvent(AccountCreatedEvent accountCreatedEvent) {
        Account account = accountAppDataMapper.accountCreatedEventToAccount(accountCreatedEvent);
        accountQueryRepository.save(account)
                .subscribe();
    }

}
