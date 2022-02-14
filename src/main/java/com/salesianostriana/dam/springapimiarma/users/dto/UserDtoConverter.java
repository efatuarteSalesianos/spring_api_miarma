package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.dto.PostDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final PostDtoConverter postDtoConverter;

    public GetUserDto convertUserEntityToGetUserDto(UserEntity user) {
        return GetUserDto.builder()
                .id(user.getId())
                .full_name(user.getFull_name())
                .direccion(user.getDireccion())
                .telefono(user.getDireccion())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .fecha_nacimiento(user.getFecha_nacimiento())
                .privacidad(user.getPrivacidad())
                .posts(user.getPosts().stream().map(postDtoConverter::postToGetPostDto).collect(Collectors.toList()))
                .numSeguidores(user.getPeticiones_seguimiento().size())
                .build();
    }

    public GetBasicUserDto convertUserEntityToGetBasicUserDto(UserEntity user) {
        return GetBasicUserDto.builder()
                .id(user.getId())
                .full_name(user.getFull_name())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .build();
    }

    public GetFollowDto convertFollowToGetFollowDto(Follow f) {
        return GetFollowDto.builder()
                .id(f.getId())
                .seguidor(this.convertUserEntityToGetBasicUserDto(f.getSeguidor()))
                .usuario(this.convertUserEntityToGetBasicUserDto(f.getUsuario()))
                .build();
    }

    public UserEntity convertCreateUserDtoToUserEntity(CreateUserDto newUser) {
        return UserEntity
                .builder()
                .full_name(newUser.getFull_name())
                .direccion(newUser.getDireccion())
                .telefono(newUser.getDireccion())
                .nickname(newUser.getNickname())
                .avatar(newUser.getAvatar())
                .password(newUser.getPassword())
                .fecha_nacimiento(newUser.getFecha_nacimiento())
                .privacidad(newUser.getPrivacidad())
                .build();
    }

}