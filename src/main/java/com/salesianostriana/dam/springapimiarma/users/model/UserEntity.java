package com.salesianostriana.dam.springapimiarma.users.model;

import com.salesianostriana.dam.springapimiarma.model.Post;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "User_Entity")
@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class UserEntity implements UserDetails, Serializable {

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

    private String full_name, direccion, telefono, avatar, password;

    private LocalDateTime fecha_nacimiento;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String nickname;

    @Column(unique = true, updatable = false)
    private String email;

    private ProfileType privacidad;

    @ManyToOne
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "seguidor", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Follow> peticiones_seguimiento = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Follow> solicitudes_seguimiento = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "seguidor_id",
            foreignKey = @ForeignKey(name="FK_SEGUIDOR")),
            inverseJoinColumns = @JoinColumn(name = "seguido_id",
                    foreignKey = @ForeignKey(name="FK_SEGUIDO")),
            name = "relaciones_user"
    )
    private List<UserEntity> seguidores = new ArrayList<>();

    @ManyToMany(mappedBy = "seguidores", fetch = FetchType.EAGER)
    private List<UserEntity> seguidos = new ArrayList<>();

    ////////////////////////////////////////////
    /* HELPERS de la asociación con Seguidor*/
    ////////////////////////////////////////////

    public void addSeguidor(UserEntity u) {
        if (this.getSeguidores() == null)
            this.setSeguidores(new ArrayList<>());
        this.getSeguidores().add(u);

        if (u.getSeguidores() == null)
            u.setSeguidores(new ArrayList<>());
        u.getSeguidores().add(this);
    }

    public void removeSeguidor(UserEntity u) {
        u.getSeguidores().remove(this);
        this.getSeguidores().remove(u);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
