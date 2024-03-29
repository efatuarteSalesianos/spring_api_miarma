package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByNickname(String nick);

    boolean existsByNickname(String nickname);

}
