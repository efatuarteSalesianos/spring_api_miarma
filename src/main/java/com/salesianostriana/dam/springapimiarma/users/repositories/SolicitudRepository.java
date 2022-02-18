package com.salesianostriana.dam.springapimiarma.users.repositories;

import com.salesianostriana.dam.springapimiarma.users.model.Solicitud;
import com.salesianostriana.dam.springapimiarma.users.model.SolicitudPK;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, SolicitudPK> {

    List<Solicitud> findAllSolicitudesByReceiver(UserEntity user);

}
