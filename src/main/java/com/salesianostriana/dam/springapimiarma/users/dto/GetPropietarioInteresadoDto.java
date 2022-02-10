package com.salesianostriana.dam.springapimiarma.users.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class GetPropietarioInteresadoDto {
    private UUID id;
    private String full_name, email, avatar, mensaje;
}
