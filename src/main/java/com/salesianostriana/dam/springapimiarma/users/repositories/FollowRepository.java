package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import com.salesianostriana.dam.springapimiarma.users.model.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
}
