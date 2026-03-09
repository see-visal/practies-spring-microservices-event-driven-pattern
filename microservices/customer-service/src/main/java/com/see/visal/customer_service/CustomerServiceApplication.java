package com.see.visal.customer_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.context.annotation.Bean;


@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(
			@Value("${service.name}") String name,
			@Value("${secret.weak-password}") String weakPassword,
			@Value("${secret.strong-password}") String strongPassword
	) {
		return args -> {
			System.out.println("Name: " + name);
			System.out.println("Weak Password: " + weakPassword);
			System.out.println("Strong Password: " + strongPassword);
		};
	}

}
