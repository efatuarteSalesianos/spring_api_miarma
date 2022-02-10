package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.Tipo;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class GetViviendaDto {
    private Long id;
    private String titulo, latlng, direccion, poblacion, provincia, descripcion, avatar;
    private double precio;
    private int codigoPostal, numHabitaciones, metrosCuadrados, numBanyos;
    private boolean tienePiscina, tieneAscensor, tieneGaraje;
    private Tipo tipo;
    private int numIntereses;
    private Long inmobiliariaId;
    private UUID propietarioId;
}
