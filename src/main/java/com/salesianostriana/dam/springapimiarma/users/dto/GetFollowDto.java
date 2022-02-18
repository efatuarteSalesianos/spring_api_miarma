package com.salesianostriana.dam.springapimiarma.users.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class GetFollowDto {

    private GetBasicUserDto follower;
    private GetBasicUserDto seguido;

}
