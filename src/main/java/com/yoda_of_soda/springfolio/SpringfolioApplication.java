package com.yoda_of_soda.springfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// @ComponentScan(basePackages = "com.yoda_of_soda.springfolio.repository")
// @ComponentScan(basePackages = "com.yoda_of_soda.springfolio.services")
public class SpringfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringfolioApplication.class, args);
	}

}
