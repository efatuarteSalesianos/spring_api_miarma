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
}
