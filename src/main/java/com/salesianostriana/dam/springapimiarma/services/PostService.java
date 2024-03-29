package com.salesianostriana.dam.springapimiarma.services;

import com.salesianostriana.dam.springapimiarma.dto.*;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.ListEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.PrivateAccountException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.SingleEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.ficheros.errores.StorageException;
import com.salesianostriana.dam.springapimiarma.ficheros.service.StorageService;
import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.repositories.PostRepository;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.model.ProfileType;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.salesianostriana.dam.springapimiarma.model.PostType.PUBLIC;

@Service
@RequiredArgsConstructor
public class PostService extends BaseService<Post, UUID, PostRepository> {

    private final PostRepository repository;
    private final UserEntityRepository userEntityRepository;
    private final PostDtoConverter dtoConverter;
    private final StorageService storageService;
    private final List<String> imagesType = new ArrayList<>(List.of("image/jpeg", "image/png", "image/jpg", "image/svg"));
    private final List<String> videosType = new ArrayList<>(List.of("video/mp4", "image/avi", "image/mkv", "image/mov"));

    public Post save(CreatePostDto newPost, MultipartFile file) {

        String filenameResize = "";
        String uriResize = "";
        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        if(imagesType.contains(file.getContentType()))
            filenameResize = storageService.storeAndResizePostImage(file);

        else if(videosType.contains(file.getContentType()))
            filenameResize = storageService.storeAndResizePostVideo(file);

        else
            throw new StorageException("Formato de fichero no válido");

        uriResize = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filenameResize)
                .toUriString();

        return repository.save(Post.builder()
                    .titulo(newPost.getTitulo())
                    .descripcion(newPost.getDescripcion())
                    .media(uri)
                    .mediaResized(uriResize)
                    .fechaPublicacion(LocalDate.now())
                    .tipo(newPost.getTipo())
                .build());
    }

    public List<GetPostListDto> findAllPublicPosts() {
        List<Post> result = repositorio.postsPublicos();

        if (result.isEmpty()) {
            throw new ListEntityNotFoundException(Post.class);
        } else {
            return result.stream().map(dtoConverter::postToGetPostListDto)
                    .collect(Collectors.toList());
        }
    }

    public GetPostDto findPostById(UUID id, UserEntity user) throws PrivateAccountException {
        Optional<Post> result = repositorio.findById(id);

        if (result.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            UserEntity usuario = result.get().getPropietario();

            if (result.get().getTipo() == PUBLIC || usuario.getSeguidores_list().contains(user))
                return result.map(dtoConverter::postToGetPostDto).get();
            else
                throw new PrivateAccountException("No puedes ver el post de este usuario, ya que no le sigues");
        }
    }

    public GetPostDto edit(UUID id, SavePostDto post, MultipartFile file) {

        String filenameResize = "";
        String uriResize = "";

        Optional<Post> encontrado = repositorio.findById(id);

        if (encontrado.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            if (!file.isEmpty()) {

                String filename = storageService.store(file);
                storageService.deleteFile(encontrado.get().getMedia().substring(encontrado.get().getMedia().lastIndexOf("/") + 1));
                String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(filename)
                        .toUriString();

                if(imagesType.contains(file.getContentType()))
                    filenameResize = storageService.storeAndResizePostImage(file);

                else if(videosType.contains(file.getContentType()))
                    filenameResize = storageService.storeAndResizePostVideo(file);

                else
                    throw new StorageException("Formato de fichero no válido");

                storageService.deleteFile(encontrado.get().getMediaResized().substring(encontrado.get().getMediaResized().lastIndexOf("/") + 1));

                uriResize = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(filenameResize)
                        .toUriString();

                    encontrado.get().setTitulo(post.getTitulo());
                    encontrado.get().setDescripcion(post.getDescripcion());
                    encontrado.get().setMedia(uri);
                    encontrado.get().setMediaResized(uriResize);

                    return dtoConverter.postToGetPostDto(encontrado.get());
                } else {
                encontrado.get().setTitulo(post.getTitulo());
                encontrado.get().setDescripcion(post.getDescripcion());
                encontrado.get().setMedia(post.getMedia());
            }
                return dtoConverter.postToGetPostDto(encontrado.get());
        }
    }

    public List<GetPostListDto> postByNickname(String nick, UserEntity user) {
        if (!userEntityRepository.existsByNickname(nick)) {
            throw new UsernameNotFoundException(nick + " no encontrado");
        } else {
            Optional<UserEntity> encontrado = userEntityRepository.findFirstByNickname(nick);
            if (encontrado.isEmpty())
                throw new SingleEntityNotFoundException(nick, UserEntity.class);
            else {
                if (encontrado.get().getPrivacidad() == ProfileType.PUBLIC || encontrado.get().getSeguidores_list().contains(user))
                    return repository.postsByUser(nick).stream().map(dtoConverter::postToGetPostListDto).collect(Collectors.toList());
                else
                    return repository.publicPostsByUser(nick).stream().map(dtoConverter::postToGetPostListDto).collect(Collectors.toList());
            }
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
