package com.salesianostriana.dam.springapimiarma.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class CreateInmobiliariaDto {

    private String nombre, email, telefono, avatar;
}
