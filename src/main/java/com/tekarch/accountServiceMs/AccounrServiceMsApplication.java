package com.tekarch.accountServiceMs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.tekarch.accountServiceMs")

@EnableFeignClients  // Enable Feign clients
@ComponentScan(basePackages = "com.tekarch.accountServiceMs")

public class AccounrServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccounrServiceMsApplication.class, args);
	}

}
