package com.salesianostriana.dam.springapimiarma.controllers;

import com.salesianostriana.dam.springapimiarma.dto.CreatePostDto;
import com.salesianostriana.dam.springapimiarma.dto.GetPostDto;
import com.salesianostriana.dam.springapimiarma.dto.PostDtoConverter;
import com.salesianostriana.dam.springapimiarma.model.Post;
import com.salesianostriana.dam.springapimiarma.services.PostService;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Post")
@Tag(name="Post", description = "Clase controladora de Posts")
public class PostController {

    private final PostService service;
    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;
    private final PostDtoConverter dtoConverter;

    @Operation(summary = "Se añade una Post")
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
    public ResponseEntity<CreatePostDto> nuevoPost(@RequestBody CreatePostDto nuevaPost) {

        service.save(nuevaPost);

        if(nuevaPost.getNombre().isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaPost);
    }

    @Operation(summary = "Se muestra un listado con todas las Posts")
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
    @GetMapping("/")
    public ResponseEntity<List<GetPostDto>> listarPosts() {
        List <GetPostDto> Posts = service.findAll()
                .stream()
                .map(dtoConverter::PostToGetPostDto)
                .collect(Collectors.toList());
        if(Posts.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();
        return ResponseEntity
                .ok()
                .body(Posts);
    }

    @Operation(summary = "Se muestran los detalles de una Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la Post correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la Post que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPostDto> buscarPost(@Parameter(description = "El id de la Post que se busca") @PathVariable UUID id) {
        return ResponseEntity
                .of(this.service.findById(id)
                .map(dtoConverter::PostToGetPostDto));
    }

    @Operation(summary = "Se borra una Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la Post que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@Parameter(description = "El id de la Post que se desea borrar") @PathVariable UUID id) {

        Optional<Post> inmo = service.findById(id);

        if(inmo.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
