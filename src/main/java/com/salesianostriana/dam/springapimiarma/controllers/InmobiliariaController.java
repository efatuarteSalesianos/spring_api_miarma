package com.salesianostriana.dam.springapimiarma.controllers;

import com.salesianostriana.dam.springapimiarma.dto.CreateInmobiliariaDto;
import com.salesianostriana.dam.springapimiarma.dto.GetInmobiliariaDto;
import com.salesianostriana.dam.springapimiarma.dto.InmobiliariaDtoConverter;
import com.salesianostriana.dam.springapimiarma.model.Inmobiliaria;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.services.InmobiliariaService;
import com.salesianostriana.dam.springapimiarma.services.ViviendaService;
import com.salesianostriana.dam.springapimiarma.users.dto.*;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.model.UserRoles;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inmobiliaria")
@Tag(name="Inmobiliaria", description = "Clase controladora de inmobiliarias")
public class InmobiliariaController {

    private final InmobiliariaService service;
    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;
    private final InmobiliariaDtoConverter dtoConverter;
    private final ViviendaService viviendaService;

    @Operation(summary = "Se añade una Inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Hay un error en los datos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CreateInmobiliariaDto> nuevaInmobiliaria(@RequestBody CreateInmobiliariaDto nuevaInmobiliaria) {

        service.save(nuevaInmobiliaria);

        if(nuevaInmobiliaria.getNombre().isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaInmobiliaria);
    }
    @Operation(summary = "Se añade un gestor a una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Hay un error en los datos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/{id}/gestor")
    public ResponseEntity<GetGestorDto> nuevoGestorEnInmobiliaria(@Parameter(description = "El id de la inmobiliaria a la que le añadimos el nuevo gestor") @PathVariable Long id, @RequestBody CreateUserDto nuevoGestor, @AuthenticationPrincipal UserEntity user) {
        if((user.getRole().equals(UserRoles.GESTOR) && user.getInmobiliaria().getId().equals(id)) || user.getRole().equals(UserRoles.ADMIN)) {
            Optional<Inmobiliaria> inmo = service.findById(id);
            if(inmo.isEmpty())
                return ResponseEntity
                        .notFound()
                        .build();
            UserEntity saved = userEntityService.save(nuevoGestor, UserRoles.GESTOR);
            if(saved == null)
                return ResponseEntity
                        .badRequest()
                        .build();
            else
                inmo.get().getGestores().add(saved);
                service.edit(inmo.get());
                saved.addToInmobiliaria(inmo.get());
                userEntityService.edit(saved);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(userDtoConverter.convertUserEntityToGetGestorDto(saved));
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se elimina un gestor de una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "El gestor buscado no existe",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/gestor/{id}")
    public ResponseEntity<?> eliminarGestorDeInmobiliaria(@Parameter(description = "El id del gestor que queremos eliminar") @PathVariable UUID id, @AuthenticationPrincipal UserEntity user) {
        Optional<UserEntity> gestor = userEntityService.findById(id);
        if(gestor.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();
        if((user.getRole().equals(UserRoles.GESTOR)) && user.getInmobiliaria().getId().equals(gestor.get().getInmobiliaria().getId()) || user.getRole().equals(UserRoles.ADMIN)) {
            gestor.get().getInmobiliaria().getGestores().remove(gestor.get());
            this.service.edit(gestor.get().getInmobiliaria());
            gestor.get().removeFromInmobiliaria(gestor.get().getInmobiliaria());
            userEntityService.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se muestra un listado de gestores de la inmobiliaria del gestor logueado en ese momento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran los gestores correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay gestores",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/gestores")
    public ResponseEntity<List<GetUserDto>> mostrarGestoresDeInmobiliaria(@AuthenticationPrincipal UserEntity user) {

        List<GetUserDto> gestores = userEntityService.findGestoresDeInmobiliaria(user.getInmobiliaria().getId())
                .stream()
                .map(userDtoConverter::convertUserEntityToGetUserDto)
                .collect(Collectors.toList());

        if (gestores.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();

        return ResponseEntity
                .ok()
                .body(gestores);
    }

    @Operation(summary = "Se muestra un listado con todas las inmobiliarias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay inmobiliarias",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<GetInmobiliariaDto>> listarInmobiliarias() {
        List <GetInmobiliariaDto> inmobiliarias = service.findAll()
                .stream()
                .map(dtoConverter::inmobiliariaToGetInmobiliariaDto)
                .collect(Collectors.toList());
        if(inmobiliarias.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();
        return ResponseEntity
                .ok()
                .body(inmobiliarias);
    }

    @Operation(summary = "Se muestran los detalles de una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la inmobiliaria correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la inmobiliaria que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetInmobiliariaDto> buscarInmobiliaria(@Parameter(description = "El id de la inmobiliaria que se busca") @PathVariable Long id) {
        return ResponseEntity
                .of(this.service.findById(id)
                .map(dtoConverter::inmobiliariaToGetInmobiliariaDto));
    }

    @Operation(summary = "Se borra una Inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la inmobiliaria que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInmobiliaria(@Parameter(description = "El id de la inmobiliaria que se desea borrar") @PathVariable Long id) {

        Optional<Inmobiliaria> inmo = service.findById(id);

        if(inmo.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();

        List<Vivienda> viviendas = viviendaService.findViviendasDeInmobiliaria(id);

        for (Vivienda v: viviendas) {
            v.removeFromInmobiliaria(inmo.get());
            viviendaService.edit(v);
        }

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Se muestra un listado de todos los usuarios con el rol gestor de una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay gestores",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}/gestores")
    public ResponseEntity<List<GetGestorDto>> getGestores(@Parameter(description = "El id de la inmobiliaria que vamos a consultar") @PathVariable Long id, @AuthenticationPrincipal UserEntity user) {
        Optional<Inmobiliaria> inmo = service.findById(id);
        if (inmo.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();
        if ((user.getRole().equals(UserRoles.GESTOR) && user.getInmobiliaria().equals(id)) || user.getRole().equals(UserRoles.ADMIN)) {
            List<GetGestorDto> gestores = userEntityService.findByRole(UserRoles.GESTOR)
                    .stream()
                    .filter(g -> g.getInmobiliaria().getId().equals(id))
                    .map(userDtoConverter::convertUserEntityToGetGestorDto)
                    .collect(Collectors.toList());
            if (gestores.isEmpty())
                return ResponseEntity.notFound().build();

            return ResponseEntity
                    .ok().body(gestores);
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();

    }
}
