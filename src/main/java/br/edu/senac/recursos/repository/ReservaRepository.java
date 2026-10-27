package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
