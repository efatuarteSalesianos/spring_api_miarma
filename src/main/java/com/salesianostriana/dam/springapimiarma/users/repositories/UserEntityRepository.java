package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.model.UserRoles;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByEmail(String email);

    @EntityGraph(value = "grafo-propietario-con-viviendas", type = EntityGraph.EntityGraphType.FETCH)
    List<UserEntity> findUserEntityByRole(UserRoles role);

    @Query("select u from UserEntity u left join u.inmobiliaria i where i.id = :inmobiliariaId")
    @EntityGraph(value = "grafo-gestor-con-inmobiliaria", type = EntityGraph.EntityGraphType.FETCH)
    List<UserEntity> findByInmobiliariaIdUsingQuery(@Param("inmobiliariaId") Long inmobiliariaId);

    @Query(value = """
        select u.* from user_entity u
        where u.role = 'PROPIETARIO' 
        and u.id in
        (select i.interesado_id
        from interesa i
        where i.interesado_id = u.id)
        """, nativeQuery = true)
    @EntityGraph(value = "grafo-propietario-con-viviendas-e-intereses", type = EntityGraph.EntityGraphType.FETCH)
    List<UserEntity> findInteresados();

}
