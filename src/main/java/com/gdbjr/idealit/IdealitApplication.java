package com.gdbjr.idealit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class IdealitApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdealitApplication.class, args);
	}

}

