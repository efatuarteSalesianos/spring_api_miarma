package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.SolicitudPK;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetSolicitudDto implements Serializable {

    private SolicitudPK id;
    private GetBasicUserDto sender;
    private GetBasicUserDto receiver;
}
