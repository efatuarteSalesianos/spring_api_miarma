package com.salesianostriana.dam.springapimiarma.model;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Interesa implements Serializable {

    @Builder.Default
    @EmbeddedId
    private InteresaPK id = new InteresaPK();

    @ManyToOne
    @MapsId("interesado_id")
    @JoinColumn(name = "interesado_id")
    private UserEntity interesado;

    @ManyToOne
    @MapsId("vivienda_id")
    @JoinColumn(name = "vivienda_id")
    private Vivienda vivienda;

    @CreatedDate
    private LocalDateTime createdDate;

    private String mensaje;

    /* HELPERS CON VIVIENDA */

    public void addToVivienda(Vivienda v) {
        vivienda = v;
        v.getIntereses().add(this);
    }

    public void removeFromVivienda(Vivienda v) {
        v.getIntereses().remove(this);
        vivienda = null;
    }

    /* HELPERS CON INTERESADO */

    public void addToInteresado(UserEntity i) {
        interesado = i;
        i.getIntereses().add(this);
    }

    public void removeFromInteresado(UserEntity i) {
        i.getIntereses().remove(this);
        interesado = null;
    }

    /* HELPERS ENTRE VIVIENDA E INTERESADO */

    public void addInteresadoVivienda(UserEntity interesado, Vivienda vivienda) {
        addToVivienda(vivienda);
        addToInteresado(interesado);
    }

    public void removeInteresadoVivienda(Vivienda vivienda, UserEntity interesado) {
        removeFromVivienda(vivienda);
        removeFromInteresado(interesado);
    }
}
