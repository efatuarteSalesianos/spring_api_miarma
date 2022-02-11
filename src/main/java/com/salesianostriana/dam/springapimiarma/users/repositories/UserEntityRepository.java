package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByEmail(String email);

    @Query("select u from UserEntity u left join u.Post i where i.id = :PostId")
    @EntityGraph(value = "grafo-gestor-con-Post", type = EntityGraph.EntityGraphType.FETCH)
    List<UserEntity> findByPostIdUsingQuery(@Param("PostId") UUID PostId);

}
