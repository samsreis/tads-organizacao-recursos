package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.Sala;

public record SalaResponse(Long id, String nome, Integer capacidade, String localizacao, Boolean emManutencao) {
    public static SalaResponse de(Sala s) {
        return new SalaResponse(s.getId(), s.getNome(), s.getCapacidade(), s.getLocalizacao(), s.getEmManutencao());
    }
}
