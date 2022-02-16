package com.salesianostriana.dam.springapimiarma.users.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Solicitud {

    @Builder.Default
    @EmbeddedId
    private SolicitudPK id = new SolicitudPK();

    @ManyToOne
    @MapsId("sender_id")
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne
    @MapsId("receiver_id")
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @CreatedDate
    private LocalDate createdDate;
}
