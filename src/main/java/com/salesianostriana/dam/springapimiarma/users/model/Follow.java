package com.salesianostriana.dam.springapimiarma.users.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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
    @MapsId("follower_id")
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @ManyToOne
    @MapsId("seguido_id")
    @JoinColumn(name = "seguido_id")
    private UserEntity seguido;

    @CreatedDate
    private LocalDate createdDate;
}
