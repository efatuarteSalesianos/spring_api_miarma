package com.salesianostriana.dam.springapimiarma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringApiMiarmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiMiarmaApplication.class, args);
	}

}
