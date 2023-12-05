package com.battlemetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RustApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RustApiApplication.class, args);
	}

}
