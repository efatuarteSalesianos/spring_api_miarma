package com.salesianostriana.dam.springapimiarma.users.model;

import com.salesianostriana.dam.springapimiarma.model.Inmobiliaria;
import com.salesianostriana.dam.springapimiarma.model.Interesa;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import org.hibernate.annotations.Parameter;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NamedEntityGraph(
        name = "grafo-propietario-con-viviendas",
        attributeNodes = {
                @NamedAttributeNode(value = "full_name"),
                @NamedAttributeNode(value = "direccion"),
                @NamedAttributeNode(value = "telefono"),
                @NamedAttributeNode(value = "avatar"),
                @NamedAttributeNode(value = "viviendas", subgraph = "subgrafo-viviendas")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "subgrafo-viviendas",
                        attributeNodes = {
                                @NamedAttributeNode("titulo"),
                                @NamedAttributeNode("latlng"),
                                @NamedAttributeNode("direccion"),
                                @NamedAttributeNode("poblacion"),
                                @NamedAttributeNode("provincia"),
                                @NamedAttributeNode("descripcion"),
                                @NamedAttributeNode("avatar"),
                                @NamedAttributeNode("codigoPostal"),
                                @NamedAttributeNode("numHabitaciones"),
                                @NamedAttributeNode("metrosCuadrados"),
                                @NamedAttributeNode("numBanyos"),
                                @NamedAttributeNode("tipo"),
                                @NamedAttributeNode("precio"),
                        })
        }
)

@NamedEntityGraph(
        name = "grafo-gestor-con-inmobiliaria",
        attributeNodes = {
                @NamedAttributeNode(value = "full_name"),
                @NamedAttributeNode(value = "direccion"),
                @NamedAttributeNode(value = "telefono"),
                @NamedAttributeNode(value = "avatar"),
                @NamedAttributeNode(value = "inmobiliaria", subgraph = "subgrafo-inmobiliaria")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "subgrafo-inmobiliaria",
                        attributeNodes = {
                                @NamedAttributeNode("nombre"),
                                @NamedAttributeNode("email"),
                                @NamedAttributeNode("telefono"),
                                @NamedAttributeNode("avatar"),
                        })
        }
)

@NamedEntityGraph(
        name = "grafo-propietario-con-viviendas-e-intereses",
        attributeNodes = {
                @NamedAttributeNode(value = "full_name"),
                @NamedAttributeNode(value = "direccion"),
                @NamedAttributeNode(value = "telefono"),
                @NamedAttributeNode(value = "avatar"),
                @NamedAttributeNode(value = "viviendas", subgraph = "subgrafo-viviendas")
        },
        subgraphs = {
                @NamedSubgraph(
                name = "subgrafo-viviendas",
                attributeNodes = {
                        @NamedAttributeNode("titulo"),
                        @NamedAttributeNode("latlng"),
                        @NamedAttributeNode("direccion"),
                        @NamedAttributeNode("poblacion"),
                        @NamedAttributeNode("provincia"),
                        @NamedAttributeNode("descripcion"),
                        @NamedAttributeNode("avatar"),
                        @NamedAttributeNode("codigoPostal"),
                        @NamedAttributeNode("numHabitaciones"),
                        @NamedAttributeNode("metrosCuadrados"),
                        @NamedAttributeNode("numBanyos"),
                        @NamedAttributeNode("tipo"),
                        @NamedAttributeNode("precio"),
                        @NamedAttributeNode(value = "intereses", subgraph = "subgrafo-intereses")
                }),
                @NamedSubgraph(
                        name = "subgrafo-intereses",
                        attributeNodes = {
                                @NamedAttributeNode("interesado"),
                                @NamedAttributeNode("createdDate"),
                                @NamedAttributeNode("mensaje")
                        }
                )
        }
)
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

    private String direccion, telefono, avatar, password, full_name;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String email;

    private UserRoles role;

    @OneToMany(mappedBy = "propietario", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Vivienda> viviendas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inmobiliaria", foreignKey = @ForeignKey(name = "FK_GESTOR_INMOBILIARIA"), nullable = true)
    private Inmobiliaria inmobiliaria;

    @OneToMany(mappedBy = "interesado", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Interesa> intereses = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
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

    /* HELPERS CON INMOBILIARIA */

    public void addToInmobiliaria(Inmobiliaria i){
        this.inmobiliaria = i;
        i.getGestores().add(this);
    }

    public void removeFromInmobiliaria(Inmobiliaria i){
        i.getGestores().remove(this);
        this.inmobiliaria = null;
    }
}
