package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("select distinct r.sala.id from Reserva r " +
           "where r.status in :status and r.inicio < :fim and r.fim > :inicio")
    List<Long> salasOcupadasNoIntervalo(
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim,
            @Param("status") List<Reserva.StatusReserva> status);
}
