package com.salesianostriana.dam.springapimiarma.services;

import com.salesianostriana.dam.springapimiarma.dto.CreateInmobiliariaDto;
import com.salesianostriana.dam.springapimiarma.dto.CreateViviendaDto;
import com.salesianostriana.dam.springapimiarma.dto.InmobiliariaDtoConverter;
import com.salesianostriana.dam.springapimiarma.model.Inmobiliaria;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.repositories.InmobiliariaRepository;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InmobiliariaService extends BaseService<Inmobiliaria, Long, InmobiliariaRepository> {

    private final InmobiliariaDtoConverter dtoConverter;

    public Inmobiliaria save(CreateInmobiliariaDto nuevaInmobiliaria) {
        Inmobiliaria i = dtoConverter.createInmobiliariaDtoToInmobiliaria(nuevaInmobiliaria);
        this.save(i);
        return i;
    }
}
