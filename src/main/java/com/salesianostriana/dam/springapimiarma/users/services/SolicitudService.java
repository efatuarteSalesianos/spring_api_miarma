package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.users.dto.GetFollowDto;
import com.salesianostriana.dam.springapimiarma.users.dto.GetSolicitudDto;
import com.salesianostriana.dam.springapimiarma.users.dto.SolicitudDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private SolicitudRepository solicitudRepository;
    private SolicitudDtoConverter solicitudDtoConverter;

    public List<GetSolicitudDto> findAllPeticionesSeguimiento(UserEntity user) {
        return solicitudRepository.findAllSolicitudesByReceiver(user).stream().map(solicitudDtoConverter::convertSolicitudToGetSolicitudDto).toList();
    }
}
