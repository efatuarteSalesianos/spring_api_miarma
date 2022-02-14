package com.salesianostriana.dam.springapimiarma;

import com.salesianostriana.dam.springapimiarma.ficheros.utils.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class SpringApiMiarmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiMiarmaApplication.class, args);
	}

}
