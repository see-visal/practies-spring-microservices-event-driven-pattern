package com.see.visal.account_service;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.jdbc.DataSourceConnectionProvider;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore;
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AxonConfig {

    @Bean
    public CommandBus commandBus() {
        return SimpleCommandBus.builder().build();
    }

    @Bean
    public Cache cache() {
        return new WeakReferenceCache();
    }

    @Bean
    public SnapshotTriggerDefinition accountSnapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
    }

    @Bean
    public TokenStore tokenStore(ConnectionProvider connectionProvider,
                                 Serializer serializer) {

        // Define the schema to match your snake_case table
        TokenSchema tokenSchema = TokenSchema.builder()
                .setTokenTable("token_entry") // Match your DB table name exactly
                // If your columns also use underscores, define them here:
                .setProcessorNameColumn("processor_name")
                .setTokenTypeColumn("token_type")
                .setTokenColumn("token")
                .setSegmentColumn("segment")
                .setOwnerColumn("owner")
                .setTimestampColumn("timestamp")
                .build();

        return JdbcTokenStore.builder()
                .schema(tokenSchema)
                .connectionProvider(connectionProvider)
                .contentType(byte[].class)
                .serializer(serializer)
                .build();
    }

    @Bean
    public ConnectionProvider connectionProvider(DataSource pgDataSource) {
        return new DataSourceConnectionProvider(pgDataSource);
    }

}
