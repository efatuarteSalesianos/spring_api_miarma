package com.salesianostriana.dam.springapimiarma.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable @NoArgsConstructor
@AllArgsConstructor @Getter
@Setter
public class FollowPK implements Serializable {

    private UUID seguidor_id;
    private UUID usuario_id;
}
