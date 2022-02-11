package com.salesianostriana.dam.springapimiarma.repositories;

import com.salesianostriana.dam.springapimiarma.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
