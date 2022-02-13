package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.users.dto.GetUserDto;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class GetPostListDto {

    private UUID id;
    private String titulo, descripcion, media;
    private GetUserDto propietario;
}
