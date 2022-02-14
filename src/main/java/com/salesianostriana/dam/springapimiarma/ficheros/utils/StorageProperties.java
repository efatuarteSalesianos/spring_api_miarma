package com.salesianostriana.dam.springapimiarma.ficheros.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
@Getter @Setter
public class StorageProperties {

    private String location;

}
