package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.MovimentacaoMaterial;

import java.time.OffsetDateTime;

public record MovimentacaoResponse(Long id, Long materialId, String tipo, Integer quantidade,
                                   String usuarioEmail, OffsetDateTime instante) {
    public static MovimentacaoResponse de(MovimentacaoMaterial m) {
        return new MovimentacaoResponse(m.getId(), m.getMaterial().getId(), m.getTipo(),
                m.getQuantidade(), m.getUsuarioEmail(), m.getInstante());
    }
}
