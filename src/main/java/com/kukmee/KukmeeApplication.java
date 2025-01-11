package com.kukmee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KukmeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KukmeeApplication.class, args);
	}

}
