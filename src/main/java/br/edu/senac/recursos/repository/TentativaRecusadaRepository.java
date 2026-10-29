package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.TentativaRecusada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TentativaRecusadaRepository extends JpaRepository<TentativaRecusada, Long> {
    List<TentativaRecusada> findByInstanteBetween(OffsetDateTime ini, OffsetDateTime fim);
}
