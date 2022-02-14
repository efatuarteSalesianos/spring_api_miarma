package com.salesianostriana.dam.springapimiarma.services;

import com.salesianostriana.dam.springapimiarma.dto.*;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.ListEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.SingleEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.ficheros.service.StorageService;
import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.repositories.PostRepository;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService extends BaseService<Post, UUID, PostRepository> {

    private final PostRepository repository;
    private final UserEntityRepository userEntityRepository;
    private final PostDtoConverter dtoConverter;
    private final StorageService storageService;

    public Post save(CreatePostDto newPost, MultipartFile file) {

        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        return repository.save(Post.builder()
                    .titulo(newPost.getTitulo())
                    .descripcion(newPost.getDescripcion())
                    .media(uri)
                    .fechaPublicacion(LocalDateTime.now())
                    .tipo(newPost.getTipo())
                .build());
    }

    public List<GetPostListDto> findAllPosts() {
        List<Post> result = repositorio.findAll();

        if (result.isEmpty()) {
            throw new ListEntityNotFoundException(Post.class);
        } else {
            return result.stream().map(dtoConverter::postToGetPostListDto)
                    .collect(Collectors.toList());
        }
    }

    public GetPostDto findPostById(UUID id, UserEntity user) {
        Optional<Post> result = repositorio.findById(id);

        if (result.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            UserEntity usuario = result.get().getPropietario();
            if (usuario.getSeguidores().contains(user))
                return result.map(dtoConverter::postToGetPostDto).get();
            else
                throw new UsernameNotFoundException("No puedes ver los post de este usuario, ya que no le sigues");
        }
    }

    public GetPostDto edit(UUID id, SavePostDto post) {

        Optional<Post> encontrado = repositorio.findById(id);

        if (encontrado.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            return encontrado.map(c -> {
                        c.setTitulo(post.getTitulo());
                        c.setDescripcion(post.getDescripcion());
                        c.setMedia(post.getMedia());
                        repositorio.save(c);
                        return c;
                    })
                    .map(dtoConverter::postToGetPostDto).get();
        }
    }

    public List<GetPostListDto> postPublicos() {
        return repository.postsPublicos().stream().map(dtoConverter::postToGetPostListDto).collect(Collectors.toList());
    }

    public List<GetPostListDto> postByNickname(String nick) {
        if (!userEntityRepository.existsByNickname(nick)) {
            throw new UsernameNotFoundException(nick + " no encontrado");
        } else {
            return repository.postsByUser(nick).stream().map(dtoConverter::postToGetPostListDto).collect(Collectors.toList());
        }
    }

    public void deleteById(UUID id) {
        Optional<Post> encontrado = repositorio.findById(id);

        if (encontrado.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else if (repositorio.findAll().isEmpty()) {
            throw new ListEntityNotFoundException(Post.class);
        } else {
            repositorio.delete(encontrado.get());
        }
    }
}
