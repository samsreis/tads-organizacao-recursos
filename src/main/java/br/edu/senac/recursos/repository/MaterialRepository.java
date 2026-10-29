package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByTipoContainingIgnoreCase(String tipo);
}
