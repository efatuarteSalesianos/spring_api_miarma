package com.salesianostriana.dam.springapimiarma.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class SolicitudPK implements Serializable {

    private UUID sender_id;
    private UUID receiver_id;
}
