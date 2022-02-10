package com.salesianostriana.dam.springapimiarma.controllers;

import com.salesianostriana.dam.springapimiarma.dto.*;
import com.salesianostriana.dam.springapimiarma.model.Interesa;
import com.salesianostriana.dam.springapimiarma.model.Tipo;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.services.ViviendaService;
import com.salesianostriana.dam.springapimiarma.uploads.PaginationLinkUtils;
import com.salesianostriana.dam.springapimiarma.users.dto.GetPropietarioInteresadoDto;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.model.UserRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vivienda")
@Tag(name="Vivienda", description = "Clase controladora de viviendas")
public class ViviendaController {

    private final ViviendaService service;
    private final ViviendaDtoConverter dtoConverter;
    private final PaginationLinkUtils paginationLinkUtils;
    private final InteresaDtoConverter interesaDtoConverter;

    @Operation(summary = "Se añade una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Hay un error en los datos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CreateViviendaDto> nuevaVivienda(@RequestBody CreateViviendaDto viviendaNueva, @AuthenticationPrincipal UserEntity user) {
        service.save(viviendaNueva, user);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(viviendaNueva);
    }

    @Operation(summary = "Se muestra un listado de todas las viviendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran correctamente todas las viviendas",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existen viviendas",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<Page<GetViviendaDto>> buscarViviendasConFiltros(@RequestParam("titulo") Optional<String> titulo,
                                                                          @RequestParam("provincia") Optional<String> provincia,
                                                                          @RequestParam("direccion") Optional<String> direccion,
                                                                          @RequestParam("poblacion") Optional<String> poblacion,
                                                                          @RequestParam("codigoPostal") Optional<Integer> codigoPostal,
                                                                          @RequestParam("numBanyos") Optional<Integer> numBanyos,
                                                                          @RequestParam("numHabitaciones") Optional<Integer> numHabitaciones,
                                                                          @RequestParam("metrosCuadrados") Optional<Integer> metrosCuadrados,
                                                                          @RequestParam("precio") Optional<Double> precio,
                                                                          @RequestParam("tienePiscina") Optional<Boolean> tienePiscina,
                                                                          @RequestParam("tieneAscensor") Optional<Boolean> tieneAscensor,
                                                                          @RequestParam("tieneGaraje") Optional<Boolean> tieneGaraje,
                                                                          @RequestParam("tipo") Optional<Tipo> tipo,
                                                                          Pageable pageable, HttpServletRequest request) {

        Page<Vivienda> resultado = service.findByArgs(titulo, provincia, direccion, poblacion, codigoPostal, numBanyos, numHabitaciones, metrosCuadrados, precio, tienePiscina, tieneAscensor, tieneGaraje, tipo, pageable);
        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Page<GetViviendaDto> dtoList = resultado.map(dtoConverter::viviendaToGetViviendaDto);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
            return ResponseEntity.ok().header("link", paginationLinkUtils.createLinkHeader(dtoList, uriBuilder)).body(dtoList);
        }
    }

    @Operation(summary = "Se muestra la información de una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran correctamente los datos de una vivienda",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la vivienda que se está buscando",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetViviendaDto> findViviendaById(@Parameter(description = "El id de la vivienda que se desea buscar") @PathVariable Long id) {
        return ResponseEntity
                .of(service.findById(id).map(dtoConverter::viviendaToGetViviendaDto));
    }

    @Operation(summary = "Se editan los datos de una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se actualizan correctamente los datos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la vivienda que se desea actualizar",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetViviendaDto> editVivienda(@RequestBody EditViviendaDto vivienda, @Parameter(description = "El id de la vivienda que se desea actualizar") @PathVariable Long id, @AuthenticationPrincipal UserEntity user) {
        if (user.getId().equals(service.findById(id).get().getPropietario().getId()) || user.getRole().equals(UserRoles.ADMIN)) {
            return ResponseEntity.of(
                    service.findById(id).map(v -> {
                                v.setTitulo(vivienda.getTitulo());
                                v.setDescripcion(vivienda.getDescripcion());
                                v.setAvatar(vivienda.getAvatar());
                                v.setLatlng(vivienda.getLatlng());
                                v.setDireccion(vivienda.getDireccion());
                                v.setCodigoPostal(vivienda.getCodigoPostal());
                                v.setPoblacion(vivienda.getPoblacion());
                                v.setProvincia(vivienda.getProvincia());
                                v.setTipo(vivienda.getTipo());
                                v.setPrecio(vivienda.getPrecio());
                                v.setNumHabitaciones(vivienda.getNumHabitaciones());
                                v.setNumBanyos(vivienda.getNumBanyos());
                                v.setTienePiscina(vivienda.isTienePiscina());
                                v.setTieneAscensor(vivienda.isTieneAscensor());
                                v.setTieneGaraje(vivienda.isTieneGaraje());
                                service.save(v);
                                return v;
                            })
                            .map(dtoConverter::viviendaToGetViviendaDto)
            );
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Se borra una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina correctamente la vivienda",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la vivienda que se desea eliminar",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteViviendaById(@Parameter(description = "El id de la vivienda que se desea eliminar") @PathVariable Long id, @AuthenticationPrincipal UserEntity user) {
        if (user.getId().equals(service.findById(id).get().getPropietario().getId()) || user.getRole().equals(UserRoles.ADMIN)) {
            if (service.findById(id).isEmpty())
                return ResponseEntity
                        .notFound()
                        .build();
            Vivienda v = service.findById(id).get();
            if (v.getInmobiliaria() != null)
                v.removeFromInmobiliaria(v.getInmobiliaria());
            v.removePropietario(v.getPropietario());
            service.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se asigna una inmobiliaria a una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se añade correctamente la inmobiliaria a la vivienda seleccionada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe o bien la vivienda o bien la inmobiliaria a asignar",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/{id}/inmobiliaria/{id2}")
    public ResponseEntity<GetViviendaDto> asignarInmobiliariaAVivienda(@Parameter(description = "La vivienda a la que se le desea asignar la inmobiliaria") @PathVariable Long id, @Parameter(description = "La inmobiliaria que vamos a asignar") @PathVariable Long id2, @AuthenticationPrincipal UserEntity user) {
        if (user.getId().equals(service.findById(id).get().getPropietario().getId()) || user.getRole().equals(UserRoles.ADMIN))
            return service.asignarInmobiliariaAVivienda(id, id2);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se desasigna una inmobiliaria a una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina correctamente la inmobiliaria a la vivienda seleccionada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe o bien la vivienda o bien la inmobiliaria a asignar",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}/inmobiliaria/{id2}")
    public ResponseEntity<?> eliminarInmobiliariaDeVivienda(@Parameter(description = "La vivienda a la que se le desea asignar la inmobiliaria") @PathVariable Long id, @Parameter(description = "La inmobiliaria que vamos a asignar") @PathVariable Long id2, @AuthenticationPrincipal UserEntity user) {
        if (user.getId().equals(service.findById(id).get().getPropietario().getId()) || user.getRole().equals(UserRoles.ADMIN))
            return service.eliminarInmobiliariaDeVivienda(id, id2);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se muestra el top de viviendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra una lista con las N mejores viviendas",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existen viviendas",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/top?n={limit}")
    public ResponseEntity<List<GetViviendaDto>> topNViviendas(@Parameter(description = "Las N mejores viviendas") @PathVariable int limit) {
        List<GetViviendaDto> topViviendas = service.topNViviendas(limit)
                .stream()
                .map(dtoConverter::viviendaToGetViviendaDto)
                .collect(Collectors.toList());
        if(topViviendas.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok().body(topViviendas);
    }

    @Operation(summary = "Se añade un interés a una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se añade correctamente el interés a la vivienda seleccionada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la vivienda que se busca",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/{id}/meinteresa")
    public ResponseEntity<GetInteresaDto> nuevoInteresado(GetPropietarioInteresadoDto propietarioDto, @Parameter(description = "La vivienda que buscamos para añadir el interés") @PathVariable Long id) {
        Interesa saved = service.createInteresa(propietarioDto, id);
        if (saved == null)
            return ResponseEntity
                    .badRequest()
                    .build();
        else
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(interesaDtoConverter.interesaToGetInteresaDto(saved));
    }

    @Operation(summary = "Se elimina un interés a una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina correctamente el interés de la vivienda seleccionada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la vivienda que se busca",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}/meinteresa")
    public ResponseEntity<?> eliminarInteres(@Parameter(description = "El id de la vivienda de la que queremos quitar el interés") @PathVariable Long id, GetPropietarioInteresadoDto interesadoDto, @AuthenticationPrincipal UserEntity user){
        Optional<Vivienda> vivienda = service.findById(id);
        if(vivienda.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();
        if((user.getRole().equals(UserRoles.PROPIETARIO) && vivienda.get().getIntereses().contains(user)) || user.getRole().equals(UserRoles.ADMIN)) {
            service.deleteInteresa(interesadoDto, id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Operation(summary = "Se muestran las viviendas de un propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestra la lista de viviendas del propietario logueado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existen viviendas",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @GetMapping("/user")
    public ResponseEntity<List<GetViviendaDto>> viviendasUser(@AuthenticationPrincipal UserEntity user) {
        List<GetViviendaDto> viviendasUser = service.viviendasPropietario(user.getId());

        if(viviendasUser.isEmpty())
            return ResponseEntity
                    .notFound()
                    .build();

        return ResponseEntity
                .ok()
                .body(viviendasUser);
    }

}
