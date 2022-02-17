package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.Follow;
import com.salesianostriana.dam.springapimiarma.users.model.Solicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudDtoConverter {

    private UserDtoConverter userDtoConverter;

    public GetSolicitudDto convertSolicitudToGetSolicitudDto(Solicitud s) {
        return GetSolicitudDto.builder()
                .id(s.getId())
                .sender(userDtoConverter.convertUserEntityToGetBasicUserDto(s.getSender()))
                .receiver(userDtoConverter.convertUserEntityToGetBasicUserDto(s.getReceiver()))
                .build();
    }
}
