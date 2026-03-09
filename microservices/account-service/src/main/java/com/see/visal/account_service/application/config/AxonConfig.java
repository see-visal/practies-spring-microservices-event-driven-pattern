package com.see.visal.account_service.application.config;

import com.see.visal.account_service.application.interceptor.AccountTypeValidationInterceptor;
import com.see.visal.account_service.application.interceptor.BranchValidationInterceptor;
import com.see.visal.account_service.application.interceptor.CustomerValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Axon Framework Configuration
 * - Configures snapshot triggers for event sourcing
 * - Registers command dispatch interceptors for validation
 */
@Configuration
@RequiredArgsConstructor
public class AxonConfig {

    private final CustomerValidationInterceptor customerValidationInterceptor;
    private final BranchValidationInterceptor branchValidationInterceptor;
    private final AccountTypeValidationInterceptor accountTypeValidationInterceptor;

    /**
     * Configure snapshot trigger for AccountAggregate
     * Takes a snapshot every 5 events to optimize aggregate loading
     */
    @Bean
    public SnapshotTriggerDefinition accountSnapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
    }

    /**
     * Register message dispatch interceptors with the CommandBus
     * These interceptors will validate commands before they reach the aggregate
     */
    @Autowired
    public void registerCommandInterceptors(CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(customerValidationInterceptor);
        commandBus.registerDispatchInterceptor(branchValidationInterceptor);
        commandBus.registerDispatchInterceptor(accountTypeValidationInterceptor);
    }

}

