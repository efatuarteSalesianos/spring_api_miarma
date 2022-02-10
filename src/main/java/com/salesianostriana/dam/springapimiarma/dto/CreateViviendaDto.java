package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.Tipo;
import lombok.*;

import javax.persistence.Column;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class CreateViviendaDto {

    private String titulo, descripcion, avatar, latlng, direccion, poblacion, provincia;
    private int codigoPostal, numHabitaciones, metrosCuadrados, numBanyos;
    private Tipo tipo;
    private double precio;
    private boolean tienePiscina, tieneAscensor, tieneGaraje;
    @Column(nullable = true)
    private Long inmobiliariaId;
}
