package com.see.visal.itp_indentity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient

@SpringBootApplication
public class ItpIndentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItpIndentityApplication.class, args);
	}

}
