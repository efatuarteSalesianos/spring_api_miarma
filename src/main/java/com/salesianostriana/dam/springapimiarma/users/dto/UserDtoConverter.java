package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.dto.ViviendaDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final ViviendaDtoConverter viviendaDtoConverter;

    public GetUserDto convertUserEntityToGetUserDto(UserEntity user) {
        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .full_name(user.getFull_name())
                .role(user.getRole().name())
                .build();
    }

    public GetPropietarioDto convertUserEntityToGetPropietarioDto(UserEntity user) {
        return GetPropietarioDto.builder()
                .id(user.getId())
                .full_name(user.getFull_name())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .num_viviendas(user.getViviendas().size())
                .viviendas(user.getViviendas().stream().map(viviendaDtoConverter::viviendaToGetViviendaDto).collect(Collectors.toList()))
                .build();
    }

    public GetGestorDto convertUserEntityToGetGestorDto(UserEntity user) {
        return GetGestorDto.builder()
                .id(user.getId())
                .full_name(user.getFull_name())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .PostId(user.getPost().getId())
                .build();
    }

    public UserEntity convertCreateUserDtoToUserEntity(CreateUserDto newUser) {
        return UserEntity
                .builder()
                .email(newUser.getEmail())
                .full_name(newUser.getFull_name())
                .avatar(newUser.getAvatar())
                .password(newUser.getPassword())
                .build();
    }

    public UserEntity convertGetPropietarioInteresadoDtoToUserEntity(GetPropietarioInteresadoDto interesado) {
        return UserEntity
                .builder()
                .id(interesado.getId())
                .full_name(interesado.getFull_name())
                .email(interesado.getEmail())
                .avatar(interesado.getAvatar())
                .build();
    }

}