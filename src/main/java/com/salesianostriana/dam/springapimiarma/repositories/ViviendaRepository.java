package com.salesianostriana.dam.springapimiarma.repositories;

import com.salesianostriana.dam.springapimiarma.dto.GetViviendaDto;
import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ViviendaRepository extends JpaRepository<Vivienda, Long>, JpaSpecificationExecutor<Vivienda> {

    @Query(value = """
            select * from vivienda v
            where v.id in
            (select v1.id
            from vivienda v1 join interesa i
            on v1.id = i.vivienda_id
            group by v1.id
            order by count(*) desc
            limit = :limit)
            """, nativeQuery = true)
    public List<Vivienda> topViviendas(@Param("limit") int limit);

    @Query(value = """
            select * from vivienda v
            where v.id in
            (select * from inmobiliaria i
            where i.id = :id)
            """, nativeQuery = true)
    public List<Vivienda> viviendasDeInmobiliaria(@Param("id") Long id);

    @Query(value = """
            select ../dto/GetViviendaDto(
            v.id, v.titulo, v.direccion, v.poblacion, v.avatar, (
                select count(*)
                from interesa i
                where i.vivienda.id = v.id
                and
                i.propietario.id = u.:id)
            ),
            (case
                when
                    (select count(*)
                    from interesa i
                    where i.vivienda.id = v.id
                    and
                    i.propietario.id = u.:id) = 1
                    then 
                        true
                    else
                        false
                end)
            from vivienda v
            join user_entity u
            on v.propietario.id = u.id
            where v.id = u.:id
            """)
    public List<Vivienda> viviendasPropietario(@Param("id") UUID id);
}
