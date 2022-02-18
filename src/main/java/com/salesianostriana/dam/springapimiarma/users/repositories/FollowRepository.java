package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import com.salesianostriana.dam.springapimiarma.users.model.FollowPK;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

    @Query(value = """
            DELETE s
            FROM Solicitud s
            WHERE s.sender = :sender
            AND s.receiver = :receiver
            """, nativeQuery = true)
    public void deleteSolicitudSeguimiento(@Param("sender") UserEntity sender, @Param("receiver") UserEntity receiver);
}
