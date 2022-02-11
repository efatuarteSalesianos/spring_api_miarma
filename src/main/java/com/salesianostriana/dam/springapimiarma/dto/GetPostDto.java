package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.users.dto.GetUserDto;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class GetPostDto {

    private Long id;
    private String nombre, email, telefono, avatar;
    private GetUserDto propietario;

}
