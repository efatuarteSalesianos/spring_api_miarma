package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.FollowPK;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetSolicitudDto implements Serializable {

    private GetBasicUserDto sender;
    private GetBasicUserDto receiver;
}
