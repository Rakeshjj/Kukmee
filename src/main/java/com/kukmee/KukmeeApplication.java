package com.kukmee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.kukmee.repository") // Adjust package name if needed
//@EntityScan(basePackages = "com.kukmee.Entity")
public class KukmeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KukmeeApplication.class, args);
	}

}
