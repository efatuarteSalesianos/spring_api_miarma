package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.errores.excepciones.SingleEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.users.dto.FollowDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.dto.GetFollowDto;
import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.FollowRepository;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowService {

    private FollowDtoConverter followDtoConverter;
    private FollowRepository followRepository;
    private UserEntityRepository userEntityRepository;

    public GetFollowDto aceptarSolicitudSeguimiento(String nick, UserEntity sender) {

        Optional<UserEntity> encontrado = userEntityRepository.findFirstByNickname(nick);

        if(encontrado.isEmpty())
            throw new SingleEntityNotFoundException(nick, UserEntity.class);
        else {
            UserEntity receiver = encontrado.get();

            Follow f = Follow.builder()
                    .follower(sender)
                    .seguido(receiver)
                    .build();

            receiver.getSeguidores_list().add(f);

            followRepository.deleteSolicitudSeguimiento(sender, receiver);

            userEntityRepository.save(receiver);
            userEntityRepository.save(sender);

            return followDtoConverter.convertFollowToGetFollowDto(f);

        }
    }

    public GetFollowDto eliminarSolicitudSeguimiento(String nick, UserEntity sender) {

        Optional<UserEntity> encontrado = userEntityRepository.findFirstByNickname(nick);

        if (encontrado.isEmpty())
            throw new SingleEntityNotFoundException(nick, UserEntity.class);
        else {
            UserEntity receiver = encontrado.get();

            followRepository.deleteSolicitudSeguimiento(sender, receiver);

            userEntityRepository.save(receiver);
            userEntityRepository.save(sender);

            Follow f = new Follow();

            return followDtoConverter.convertFollowToGetFollowDto(f);

        }
    }


}
