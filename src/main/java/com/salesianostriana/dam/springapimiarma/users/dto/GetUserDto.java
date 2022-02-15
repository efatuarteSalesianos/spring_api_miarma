package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.dto.GetPostDto;
import com.salesianostriana.dam.springapimiarma.users.model.ProfileType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class GetUserDto {

    private UUID id;
    private String full_name, direccion, telefono, email, nickname, avatar;
    private LocalDate fecha_nacimiento;
    private ProfileType privacidad;
    private List<GetPostDto> posts;
    private int numSeguidores;

}
