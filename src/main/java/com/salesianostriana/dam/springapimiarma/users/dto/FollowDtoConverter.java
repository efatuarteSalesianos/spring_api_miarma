package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowDtoConverter {

    private UserDtoConverter userDtoConverter;

    public GetFollowDto convertFollowToGetFollowDto(Follow f) {
        return GetFollowDto.builder()
                .follower(userDtoConverter.convertUserEntityToGetBasicUserDto(f.getFollower()))
                .seguido(userDtoConverter.convertUserEntityToGetBasicUserDto(f.getSeguido()))
                .build();
    }

}
