package com.salesianostriana.dam.springapimiarma.model;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Post implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

        private String titulo, descripcion, media, mediaResized;

    @CreatedDate
    private LocalDate fechaPublicacion;

    private PostType tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id")
    private UserEntity propietario;
}
