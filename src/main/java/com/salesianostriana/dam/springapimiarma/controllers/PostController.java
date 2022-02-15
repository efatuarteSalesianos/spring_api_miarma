package com.salesianostriana.dam.springapimiarma.controllers;

import com.salesianostriana.dam.springapimiarma.dto.*;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.PrivateAccountException;
import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.services.PostService;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name="Post", description = "Clase controladora de Posts")
public class PostController {

    private final PostService service;
    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;
    private final PostDtoConverter dtoConverter;

    @Operation(summary = "Se añade un Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Hay un error en los datos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Post> nuevoPost(@Valid @RequestBody CreatePostDto nuevoPost, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(nuevoPost, file));
    }

    @Operation(summary = "Se muestra un listado con todos los Posts públicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay Posts",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/public")
    public List<GetPostListDto> listPostPublicos() {
        return service.findAllPublicPosts();
    }

    @Operation(summary = "Se muestra un listado con todos los Posts públicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay Posts",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{nickname}")
    public List<GetPostListDto> listPostsByNick(@PathVariable String nickname, @AuthenticationPrincipal UserEntity user) {
        return service.postByNickname(nickname, user);
    }

    @Operation(summary = "Se muestra un listado con todos los Posts del usuario logueado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay Posts",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/me")
    public List<GetPostListDto> listPostsMe(@AuthenticationPrincipal UserEntity user) {
        return service.postByNickname(user.getUsername(), user);
    }

    @Operation(summary = "Se muestran los detalles de un Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra el Post correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe el Post que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetPostDto buscarPost(@Parameter(description = "El id del Post que se busca") @PathVariable UUID id, @AuthenticationPrincipal UserEntity user) throws PrivateAccountException {
        return service.findPostById(id, user);

    }

    @Operation(summary = "Se editan los detalles de un Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra el Post correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe el Post que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetPostDto editarPost(@Parameter(description = "El id del Post que se busca") @PathVariable UUID id, @Valid @RequestBody SavePostDto post) {
        return service.edit(id, post);

    }

    @Operation(summary = "Se borra un Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe el Post que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@Parameter(description = "El id del Post que se desea borrar") @PathVariable UUID id) {

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
