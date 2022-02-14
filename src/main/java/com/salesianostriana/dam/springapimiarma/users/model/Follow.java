package com.salesianostriana.dam.springapimiarma.users.model;

import lombok.*;
import org.apache.catalina.User;
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
public class Follow implements Serializable {

    @Builder.Default
    @EmbeddedId
    private FollowPK id = new FollowPK();

    @ManyToOne
    @MapsId("seguidor_id")
    @JoinColumn(name = "seguidor_id")
    private UserEntity seguidor;

    @ManyToOne
    @MapsId("usuario_id")
    @JoinColumn(name = "usuario_id")
    private UserEntity usuario;

    @CreatedDate
    private LocalDateTime createdDate;

    /* HELPERS CON SEGUIDOR */

    public void addToSeguidor(UserEntity s) {
        seguidor = s;
        s.getSolicitudes_seguimiento().add(this);
    }

    public void removeFromSeguidor(UserEntity s) {
        s.getSolicitudes_seguimiento().remove(this);
        seguidor = null;
    }

    /* HELPERS CON USUARIO */

    public void addToUsuario(UserEntity u) {
        usuario = u;
        u.getPeticiones_seguimiento().add(this);
    }

    public void removeFromUsuario(UserEntity u) {
        u.getPeticiones_seguimiento().remove(this);
        usuario = null;
    }

    /* HELPERS ENTRE SEGUIDOR Y USUARIO */

    public void addSeguidorToUsuario(UserEntity seguidor, UserEntity usuario) {
        addToSeguidor(seguidor);
        addToUsuario(usuario);
    }

    public void removeSeguidorFromUsuario(UserEntity seguidor, UserEntity usuario) {
        removeFromSeguidor(seguidor);
        removeFromUsuario(usuario);
    }
}
