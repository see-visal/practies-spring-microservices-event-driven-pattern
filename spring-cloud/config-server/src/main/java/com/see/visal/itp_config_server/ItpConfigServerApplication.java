package com.see.visal.itp_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ItpConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItpConfigServerApplication.class, args);
	}

}
