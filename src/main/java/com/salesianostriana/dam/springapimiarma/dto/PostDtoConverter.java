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

    private final ViviendaDtoConverter viviendaDtoConverter;
    private final UserDtoConverter userDtoConverter;

    public Post createPostDtoToPost(CreatePostDto Post) {
        return Post
                .builder()
                .nombre(Post.getNombre())
                .email(Post.getEmail())
                .telefono(Post.getTelefono())
                .avatar(Post.getAvatar())
                .build();
    }

    public GetPostDto PostToGetPostDto(Post i) {
        return GetPostDto
                .builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .email(i.getEmail())
                .telefono(i.getTelefono())
                .avatar(i.getAvatar())
                .viviendas(i.getViviendas().stream().map(viviendaDtoConverter::viviendaToGetViviendaDto).collect(Collectors.toList()))
                .gestores(i.getGestores().stream().map(userDtoConverter::convertUserEntityToGetUserDto).collect(Collectors.toList()))
                .build();
    }
}
