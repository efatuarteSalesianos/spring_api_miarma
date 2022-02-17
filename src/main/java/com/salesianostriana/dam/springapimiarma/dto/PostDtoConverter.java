package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PostDtoConverter {

    public Post createPostDtoToPost(CreatePostDto post) {
        return Post.builder()
                .titulo(post.getTitulo())
                .descripcion(post.getDescripcion())
                .media(post.getMedia())
                .tipo(post.getTipo())
                .build();
    }

    public GetPostDto postToGetPostDto(Post p) {
        return GetPostDto
                .builder()
                .id(p.getId())
                .titulo(p.getTitulo())
                .descripcion(p.getDescripcion())
                .media(p.getMedia())
                .tipo(p.getTipo())
                .build();
    }

    public GetPostListDto postToGetPostListDto(Post p) {
        return GetPostListDto
                .builder()
                .id(p.getId())
                .titulo(p.getTitulo())
                .descripcion(p.getDescripcion())
                .mediaResize(p.getMediaResized())
                .build();
    }
}
