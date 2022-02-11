package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostDtoConverter {

    private final UserDtoConverter userDtoConverter;

    public Post createPostDtoToPost(CreatePostDto post) {
        return Post.builder()
                .titulo(post.getTitulo())
                .descripcion(post.getDescripcion())
                .media(post.getMedia())
                .tipo(post.getTipo())
                .build();
    }

    public GetPostDto PostToGetPostDto(Post p) {
        return GetPostDto
                .builder()
                .id(p.getId())
                .titulo(p.getTitulo())
                .descripcion(p.getDescripcion())
                .media(p.getMedia())
                .tipo(p.getTipo())
                .propietario(userDtoConverter(p.getPropietario()))
                .build();
    }
}
