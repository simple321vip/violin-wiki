package com.g.estate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class GEstateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GEstateApplication.class, args);
	}

}
