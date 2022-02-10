package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.Inmobiliaria;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InmobiliariaDtoConverter {

    private final ViviendaDtoConverter viviendaDtoConverter;
    private final UserDtoConverter userDtoConverter;

    public Inmobiliaria createInmobiliariaDtoToInmobiliaria(CreateInmobiliariaDto inmobiliaria) {
        return Inmobiliaria
                .builder()
                .nombre(inmobiliaria.getNombre())
                .email(inmobiliaria.getEmail())
                .telefono(inmobiliaria.getTelefono())
                .avatar(inmobiliaria.getAvatar())
                .build();
    }

    public GetInmobiliariaDto inmobiliariaToGetInmobiliariaDto(Inmobiliaria i) {
        return GetInmobiliariaDto
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
