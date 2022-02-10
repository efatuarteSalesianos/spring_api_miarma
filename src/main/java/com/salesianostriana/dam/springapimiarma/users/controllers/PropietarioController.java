package com.salesianostriana.dam.springapimiarma.users.controllers;

import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.users.dto.GetPropietarioDto;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
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
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping("/propietario")
@Tag(name="Propietario", description = "Clase controladora de propietarios")
public class PropietarioController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @Operation(summary = "Se muestra una lista de todos los propietarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista de propietarios correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La lista de propietarios está vacía",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<GetPropietarioDto>> getPropietarios() {
        List<GetPropietarioDto> propietarios = userEntityService.findByRole(UserRoles.PROPIETARIO)
                .stream()
                .map(userDtoConverter::convertUserEntityToGetPropietarioDto)
                .collect(Collectors.toList());
        if(propietarios.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok().body(propietarios);
    }

    @Operation(summary = "Se muestran los datos de un propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran los datos correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe el propietario que se busca",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPropietarioDto> findPropietarioById(@AuthenticationPrincipal UserEntity user, @Parameter(description = "El id del propietario que se desea consultar") @PathVariable UUID id) {
        if(user.getId().equals(id) || user.getRole().equals(UserRoles.ADMIN))
            return ResponseEntity
                    .of(userEntityService.findById(id)
                            .map(userDtoConverter::convertUserEntityToGetPropietarioDto));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Se borra un propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina el propietario correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe el propietario que se busca",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePropietarioById(@AuthenticationPrincipal UserEntity user, @Parameter(description = "El id del propietario que se quiere eliminar") @PathVariable UUID id){
        if(user.getId().equals(id) || user.getRole().equals(UserRoles.ADMIN)) {
            if(userEntityService.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
            }
            else {
                userEntityService.deleteById(id);
                return ResponseEntity
                        .noContent()
                        .build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
