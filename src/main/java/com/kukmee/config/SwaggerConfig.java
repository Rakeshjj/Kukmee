package com.kukmee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Kukmee API Documentation").version("1.0")
				.description("API documentation for the Kukmee Online Food Ordering Application")
				.contact(new Contact().name("RakeshDurai").email("rakesh.d@arkitin.com")));
	}

}
