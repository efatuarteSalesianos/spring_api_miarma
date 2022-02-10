package com.salesianostriana.dam.springapimiarma.users.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class GetUserDto {

    private UUID id;
    private String email, avatar, full_name, role;

}
