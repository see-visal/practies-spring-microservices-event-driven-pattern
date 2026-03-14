package com.see_visal.accountQuery.message.message;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfigurer;

import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AxonKafkaConfig {

    private static final String PROCESSOR_NAME = "account-group";

    @Autowired
    public void configureStreamableKafkaSource(EventProcessingConfigurer eventProcessingConfigurer,
                                               StreamableKafkaMessageSource<String, byte[]> stringStreamableKafkaMessageSource){
        log.info("configureStreamableKafkaSource for process group: {}", PROCESSOR_NAME);
        eventProcessingConfigurer.registerPooledStreamingEventProcessor(
                PROCESSOR_NAME,
                configuration -> stringStreamableKafkaMessageSource
        );
    }

}
