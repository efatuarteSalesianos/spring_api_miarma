package com.salesianostriana.dam.springapimiarma.repositories;

import com.salesianostriana.dam.springapimiarma.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(value = """
            SELECT p
            FROM Post p
            WHERE p.privacidad = PUBLIC
            """, nativeQuery = true)
    public List<Post> postsPublicos();

    @Query(value = """
            SELECT p
            FROM Post p
            WHERE p.privacidad = PUBLIC
            AND p.propietario = :nickname
            """, nativeQuery = true)
    public List<Post> publicPostsByUser(@Param("nickname") String nickname);

    @Query(value = """
            SELECT p
            FROM Post p
            WHERE p.propietario = :nickname
            """, nativeQuery = true)
    public List<Post> postsByUser(@Param("nickname") String nickname);
}
