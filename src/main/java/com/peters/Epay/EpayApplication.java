package com.peters.Epay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EpayApplication {
	public static void main(String[] args) {
		SpringApplication.run(EpayApplication.class, args);
	}

}
