package com.yoda_of_soda.springfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @ComponentScan(basePackages = "com.yoda_of_soda.springfolio.repository")
// @ComponentScan(basePackages = "com.yoda_of_soda.springfolio.services")
@EnableJpaRepositories(basePackages = "com.yoda_of_soda.springfolio.repository")
public class SpringfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringfolioApplication.class, args);
	}

}
