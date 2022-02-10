package com.salesianostriana.dam.springapimiarma.model;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Vivienda implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String titulo, latlng, direccion, poblacion, provincia, descripcion, avatar;
    private int codigoPostal, numHabitaciones, metrosCuadrados, numBanyos;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private double precio;
    private boolean tienePiscina, tieneAscensor, tieneGaraje;

    @ManyToOne
    private UserEntity propietario;

    @Builder.Default
    @OneToMany(mappedBy = "vivienda", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Interesa> intereses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "inmobiliaria", foreignKey = @ForeignKey(name = "FK_VIVIENDA_INMOBILIARIA"), nullable = true)
    private Inmobiliaria inmobiliaria;

    /* HELPERS CON PROPIETARIO */

    public void addPropietario(UserEntity p) {
        this.propietario = p;
        p.getViviendas().add(this);
    }

    public void removePropietario(UserEntity p) {
        p.getViviendas().remove(this);
        this.propietario = null;
    }

    /* HELPERS CON INMOBILIARIA */

    public void addToInmobiliaria(Inmobiliaria i){
        this.inmobiliaria = i;
        i.getViviendas().add(this);
    }

    public void removeFromInmobiliaria(Inmobiliaria i){
        i.getViviendas().remove(this);
        this.inmobiliaria = null;
    }
}
