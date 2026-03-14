package com.see_visal.accountQuery.message.listener.axonkafka;



import com.see_visal.common.domain.event.AccountCreatedEvent;
import com.see_visal.accountQuery.application.ports.input.message.listener.AccountMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;



@Component
@Slf4j
@RequiredArgsConstructor
@ProcessingGroup("account-group")
public class AccountAxonKafkaListener   {

    private final AccountMessageListener accountMessageListener;

    @EventHandler
    public void on (  AccountCreatedEvent accountCreatedEvent){
        log.info("On accountCreatedEvent: {}", accountCreatedEvent);
        accountMessageListener.onAccountCreatedEvent(accountCreatedEvent);
    }
}
