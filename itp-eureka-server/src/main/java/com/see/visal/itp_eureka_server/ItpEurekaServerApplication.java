package com.see.visal.itp_eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ItpEurekaServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ItpEurekaServerApplication.class, args);
	}

}
