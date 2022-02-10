package com.salesianostriana.dam.springapimiarma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class InteresaPK implements Serializable {
    private Long vivienda_id;
    private Long interesado_id;
}