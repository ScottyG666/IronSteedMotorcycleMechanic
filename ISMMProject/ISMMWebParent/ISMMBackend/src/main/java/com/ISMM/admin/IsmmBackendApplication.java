package com.ISMM.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ISMM.common.domain", "com.ISMM.admin"})
public class IsmmBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsmmBackendApplication.class, args);
	}

}
