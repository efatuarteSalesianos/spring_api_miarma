package com.salesianostriana.dam.springapimiarma.users.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class GetBasicUserDto {

    private UUID id;
    private String full_name, nickname, avatar;
}
