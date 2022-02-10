package com.salesianostriana.dam.springapimiarma.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class GetInteresaDto {

    private UUID interesadoId;
    private Long viviendaId;
    private LocalDateTime createdDate;
    private String mensaje;
}