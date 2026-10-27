package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}
