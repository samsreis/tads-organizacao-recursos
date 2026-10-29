package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.AuditoriaReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaReservaRepository extends JpaRepository<AuditoriaReserva, Long> {
    List<AuditoriaReserva> findByReservaIdOrderByInstanteAsc(Long reservaId);
}
