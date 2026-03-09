package com.see.visal.customer_service.rest;

import com.see.visal.customer_service.application.config.DeadLetterProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/dlq")
@RequiredArgsConstructor
public class DeadLetterProcessorController {


    private final DeadLetterProcessor deadLetterProcessor;

    @PostMapping("/{processing-group}/any" )
    public CompletableFuture<Boolean> processAny(@PathVariable("processing-group") String processingGroup) {
        return deadLetterProcessor.processorAnyFor(processingGroup);
    }





}
