package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.Professor;

public record ProfessorResponse(Long id, String nome, String email, String competencia, Boolean ativo) {
    public static ProfessorResponse de(Professor p) {
        return new ProfessorResponse(p.getId(), p.getNome(), p.getEmail(), p.getCompetencia(), p.getAtivo());
    }
}
