package com.salesianostriana.dam.springapimiarma.services;

import com.salesianostriana.dam.springapimiarma.dto.CreatePostDto;
import com.salesianostriana.dam.springapimiarma.dto.PostDtoConverter;
import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.repositories.PostRepository;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService extends BaseService<Post, Long, PostRepository> {

    private final PostDtoConverter dtoConverter;

    public Post save(CreatePostDto nuevaPost) {
        Post i = dtoConverter.createPostDtoToPost(nuevaPost);
        this.save(i);
        return i;
    }
}
