package com.salesianostriana.dam.springapimiarma.repositories;

import com.salesianostriana.dam.springapimiarma.model.Interesa;
import com.salesianostriana.dam.springapimiarma.model.InteresaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface InteresaRepository extends JpaRepository<Interesa, InteresaPK> {

    @Query(value = """
            SELECT i
            FROM Interesa i
            WHERE i.interesado.id = :id
              AND  i.vivienda.id = :id2
            """, nativeQuery = true)
    Interesa findOne(@Param("id") UUID interesadoId, @Param("id2") Long viviendaId);
}
