package com.see.visal.front_bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication

public class FrontBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontBffApplication.class, args);
	}

}
