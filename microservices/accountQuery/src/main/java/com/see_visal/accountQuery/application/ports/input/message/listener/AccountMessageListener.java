package com.see_visal.accountQuery.application.ports.input.message.listener;


import com.see_visal.common.domain.event.AccountCreatedEvent;

public interface AccountMessageListener {

    void onAccountCreatedEvent(AccountCreatedEvent accountCreatedEvent);

}
