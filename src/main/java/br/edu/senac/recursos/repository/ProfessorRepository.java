package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);
    List<Professor> findByCompetenciaContainingIgnoreCase(String competencia);
}
