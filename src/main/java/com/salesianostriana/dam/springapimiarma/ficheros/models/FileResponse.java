package com.salesianostriana.dam.springapimiarma.ficheros.models;

import lombok.*;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class FileResponse {

    private String name;
    private String uri;
    private String type;
    private long size;

}