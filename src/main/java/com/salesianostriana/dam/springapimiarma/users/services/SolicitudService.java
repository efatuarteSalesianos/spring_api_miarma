package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.errores.excepciones.SingleEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.SolicitudAlreadyExistException;
import com.salesianostriana.dam.springapimiarma.users.dto.GetSolicitudDto;
import com.salesianostriana.dam.springapimiarma.users.dto.SolicitudDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.Solicitud;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.SolicitudRepository;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    private SolicitudRepository solicitudRepository;
    private SolicitudDtoConverter solicitudDtoConverter;
    private UserEntityRepository userEntityRepository;

    public List<GetSolicitudDto> findAllPeticionesSeguimiento(UserEntity user) {
        return solicitudRepository.findAllSolicitudesByReceiver(user).stream().map(solicitudDtoConverter::convertSolicitudToGetSolicitudDto).toList();
    }

    public GetSolicitudDto sendSolicitudSeguimiento(UserEntity user, String username) throws SolicitudAlreadyExistException {

        Optional<UserEntity> userOptional = userEntityRepository.findFirstByNickname(username);

        Solicitud solicitud = new Solicitud();

        if (userOptional.isEmpty())
            throw new SingleEntityNotFoundException(username, UserEntity.class);
        else {

            UserEntity receiver = userOptional.get();

            solicitud = Solicitud.builder()
                    .sender(user)
                    .receiver(receiver)
                    .build();

            if(receiver.getSolicitudes_seguimiento().contains(solicitud))
                throw new SolicitudAlreadyExistException("Ya has enviado una solicitud a este usuario");
            else {
                receiver.getSolicitudes_seguimiento().add(solicitud);
            }

        }

        return solicitudDtoConverter.convertSolicitudToGetSolicitudDto(solicitud);
    }
}
