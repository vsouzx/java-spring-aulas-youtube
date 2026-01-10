package br.com.souza.rest_order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RestOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestOrderServiceApplication.class, args);
	}

}
