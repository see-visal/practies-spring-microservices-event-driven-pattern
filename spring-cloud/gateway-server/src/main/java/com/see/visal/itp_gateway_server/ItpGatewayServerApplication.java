package com.see.visal.itp_gateway_server;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.TimeZone;

@SpringBootApplication
public class ItpGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItpGatewayServerApplication.class, args);
	}
	@PostConstruct
	public void init() {
		// Setting the default timezone to Phnom Penh
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("apiKey"))
				.defaultIfEmpty("anonymous");
	}

}
