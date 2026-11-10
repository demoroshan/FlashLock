package com.flashlock;
import org.springframework.retry.annotation.EnableRetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableRetry
public class FlashLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashLockApplication.class, args);
	}

}
