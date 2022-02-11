package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    public GetUserDto convertUserEntityToGetUserDto(UserEntity user) {
        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .full_name(user.getFull_name())
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

}