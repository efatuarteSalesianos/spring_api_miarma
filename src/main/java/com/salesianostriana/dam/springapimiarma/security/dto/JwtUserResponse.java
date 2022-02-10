package com.salesianostriana.dam.springapimiarma.security.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JwtUserResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String avatar;
    private String role;
    private String token;

}
