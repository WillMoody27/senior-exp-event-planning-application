package com.cs4360msudenver.ueventspringbootbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UEventSpringBootBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UEventSpringBootBackendApplication.class, args);
	}

}
