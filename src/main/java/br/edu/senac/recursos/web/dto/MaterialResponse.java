package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.Material;

public record MaterialResponse(Long id, String nome, String tipo, Integer quantidade, Boolean emManutencao) {
    public static MaterialResponse de(Material m) {
        return new MaterialResponse(m.getId(), m.getNome(), m.getTipo(), m.getQuantidade(), m.getEmManutencao());
    }
}
