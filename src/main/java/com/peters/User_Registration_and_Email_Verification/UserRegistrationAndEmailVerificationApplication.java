package com.peters.User_Registration_and_Email_Verification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserRegistrationAndEmailVerificationApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationAndEmailVerificationApplication.class, args);
	}

}
